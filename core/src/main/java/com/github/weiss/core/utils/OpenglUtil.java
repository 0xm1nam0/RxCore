package com.github.weiss.core.utils;

import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

public class OpenglUtil {

	private static final String LOGTAG = "HiScene";

	static int initShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType);
		checkGLError("glCreateShader(create)");

		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);

			int[] glStatusVar = { GLES20.GL_FALSE };
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, glStatusVar, 0);
			if (glStatusVar[0] == GLES20.GL_FALSE) {
				Log.e(LOGTAG, "Could NOT compile shader " + shaderType + " : " + GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}

		}

		return shader;
	}

	public static int createProgramFromShaderSrc(String vertexShaderSrc, String fragmentShaderSrc) {
		int vertShader = initShader(GLES20.GL_VERTEX_SHADER, vertexShaderSrc);
		int fragShader = initShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSrc);

		if (vertShader == 0 || fragShader == 0)
			return 0;

		int program = GLES20.glCreateProgram();
		if (program != 0) {
			GLES20.glAttachShader(program, vertShader);
			checkGLError("glAttchShader(vert)");

			GLES20.glAttachShader(program, fragShader);
			checkGLError("glAttchShader(frag)");

			GLES20.glLinkProgram(program);
			int[] glStatusVar = { GLES20.GL_FALSE };
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, glStatusVar, 0);
			if (glStatusVar[0] == GLES20.GL_FALSE) {
				Log.e(LOGTAG, "Could NOT link program : " + GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}

		return program;
	}

	public static void checkGLError(String op) {
		for (int error = GLES20.glGetError(); error != 0; error = GLES20.glGetError())
			Log.e(LOGTAG, "After operation " + op + " got glError 0x" + Integer.toHexString(error));
	}

	public static ByteBuffer makeByteBuffer(int size) {
		ByteBuffer bb = ByteBuffer.allocateDirect(size);
		bb.position(0);
		return bb;
	}

	public static ByteBuffer makeDoubleBuffer(double[] array) {
		ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length); // each
																		// float
																		// takes
																		// 4
																		// bytes
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (double d : array) {
			bb.putFloat((float) d);
		}
		bb.rewind();
		return bb;
	}

	public static ByteBuffer makeByteBuffer(short[] array) {
		ByteBuffer bb = ByteBuffer.allocateDirect(2 * array.length); // each
																		// short
																		// takes
																		// 2
																		// bytes
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (short s : array) {
			bb.putShort(s);
		}
		bb.rewind();
		return bb;
	}

	/**
	 * Get the optimal preview size for the given screen size.
	 * 
	 * @param sizes
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static Size getOptimalPreviewSize(List<Size> sizes, int screenWidth, int screenHeight) {
		float aspectRatio = ((float) screenWidth) / screenHeight;
		Size optimalSize = null;
		Collections.reverse(sizes);
		for (Size size : sizes) {
			float sizeRatio = (float) size.width / size.height;
			if (sizeRatio == aspectRatio && size.height > 400 && size.height < 1000) {
				optimalSize = size;
				return optimalSize;
			}
			if (sizeRatio == 4.0f / 3.0f && size.height > 300) {
				optimalSize = size;
			}
		}
		return optimalSize;
	}
}
