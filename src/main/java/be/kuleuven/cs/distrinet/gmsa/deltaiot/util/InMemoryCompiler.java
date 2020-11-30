package be.kuleuven.cs.distrinet.gmsa.deltaiot.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;

import javax.tools.JavaFileObject.Kind;

/**
 * 
 * Loosely based on
 * https://www.soulmachine.me/blog/2015/07/22/compile-and-run-java-source-code-in-memory/
 * 
 * @author koeny
 *
 */
public class InMemoryCompiler {

	private static class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

		protected MemoryFileManager(StandardJavaFileManager fileManager) {
			super(fileManager);
		}

		private static URI toURI(String fileName) {
			File sourceFile = new File(fileName);
			if (sourceFile.exists()) {
				return sourceFile.toURI();
			} else {
				String extension = "";
				if (fileName.toLowerCase().endsWith(".java")) {
					fileName = fileName.substring(0, fileName.length() - 5);
					extension = ".java";
					return URI.create("mfm-source:///" + fileName.replace('.', '/') + extension);
				} else {
					return URI.create("mfm-class://" + fileName);
				}
			}
		}

		public static class StringBasedJavaFileObject extends SimpleJavaFileObject {

			private final String content;

			protected StringBasedJavaFileObject(String content, String filename) {
				super(toURI(filename), JavaFileObject.Kind.SOURCE);
				this.content = content;
			}

			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
				return CharBuffer.wrap(content);
			}

			@Override
			public String toString() {
				return toUri() + " (String-based Java File Object)";
			}

		}

		public static class ByteArrayBasedJavaFileObject extends SimpleJavaFileObject {

			private byte[] content;

			protected ByteArrayBasedJavaFileObject(String filename) {
				super(toURI(filename), JavaFileObject.Kind.CLASS);
			}

			@Override
			public OutputStream openOutputStream() throws IOException {
				return new FilterOutputStream(new ByteArrayOutputStream()) {
					@Override
					public void close() throws IOException {
						out.close();
						var bos = (java.io.ByteArrayOutputStream) out;
						content = bos.toByteArray();
					}
				};
			}

			@Override
			public InputStream openInputStream() throws IOException {
				return new ByteArrayInputStream(content);
			}

			@Override
			public String toString() {
				return toUri() + " (ByteArray-based Java File Object)";
			}
		}

		private final Map<String, ByteArrayBasedJavaFileObject> files = new HashMap<>();

		public ByteArrayBasedJavaFileObject getBinaryFile(String classname) {
			return files.computeIfAbsent(classname, key -> new ByteArrayBasedJavaFileObject(key));
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
				throws IOException {
			if (kind == Kind.CLASS) {
				return getBinaryFile(className);
			}
			return super.getJavaFileForInput(location, className, kind);
		}

		public JavaFileObject makeFromSourceString(String code, String filename) {
			return new StringBasedJavaFileObject(code, filename);
		}
		
		public ClassLoader getClassLoader(Location location) {
			return new ClassLoader(Thread.currentThread().getContextClassLoader()) {
				@Override
				protected Class<?> findClass(String name) throws ClassNotFoundException {
					if (files.containsKey(name)) {
						byte[] bytes = getBinaryFile(name).content;
						return defineClass(name, bytes, 0, bytes.length);
					}
					return super.findClass(name);
				}
			};
		};
	}

	public static AdaptationAPI compileAndCreateInstance(final AdaptationModel adaptationModel) {
		String code = adaptationModel.getCode();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		var fm = new MemoryFileManager(compiler.getStandardFileManager(null, null, null));

		var diag = new DiagnosticCollector<JavaFileObject>();
		var compUnits = new ArrayList<JavaFileObject>();
		compUnits.add(fm.makeFromSourceString(code, adaptationModel.getFilename()));

		var task = compiler.getTask(new OutputStreamWriter(System.err), fm, diag, null, null, compUnits);

		if (!task.call()) {
			for (var d : diag.getDiagnostics()) {
				System.err.print(d);
			}
		}

		ClassLoader classLoader = fm.getClassLoader(null);
		try {
			Class<? extends AdaptationAPI> cls = Class.forName(adaptationModel.getClassName(), true, classLoader)
					.asSubclass(AdaptationAPI.class);
			AdaptationAPI instance = cls.getDeclaredConstructor().newInstance();
			return instance;
		} catch (Exception e) {
			throw new RuntimeException("Could not instantiate class", e);
		}
	}

}
