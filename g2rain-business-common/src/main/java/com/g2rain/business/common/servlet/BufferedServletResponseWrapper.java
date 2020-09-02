package com.g2rain.business.common.servlet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @ClassName BufferedServletResponseWrapper
 * @Description 重新输出流
 *
 * @date 2017年8月11日 下午3:36:13
 */
public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {

    private static Logger logger = LoggerFactory.getLogger(BufferedServletResponseWrapper.class);

    private ByteArrayOutputStream bos;
    private PrintWriter printWriter;

    public BufferedServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.bos = new ByteArrayOutputStream();
        this.printWriter = new PrintWriter(bos);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriterWrapper(super.getWriter(), this.printWriter);
    }

    private ServletOutputStream getSuperOutputStream() throws IOException {
        return super.getOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        ServletOutputStream servletOutputStream = new ServletOutputStreamWrapper(this);
        return servletOutputStream;
    }

    /**
     * 获取结果值
     *
     * @Title getResult
     * @return String
     *
     * @date 2017年8月11日 下午3:41:09
     */
    public String getResult() {
        byte[] bytes = this.bos.toString().getBytes();
        try {
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            logger.error("getResult error", e);
            return null;
        }
    }

    class ServletOutputStreamWrapper extends ServletOutputStream {

        private BufferedServletResponseWrapper response;

        ServletOutputStreamWrapper(BufferedServletResponseWrapper response) {
            this.response = response;
        }

        /* (non-Javadoc)
         * @see java.io.OutputStream#write(int)
         */
        @Override
        public void write(int b) throws IOException {
            response.bos.write(b);
            response.getSuperOutputStream().write(b);
        }

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
		}

    }

    class PrintWriterWrapper extends PrintWriter {

        private PrintWriter branch;

        public PrintWriterWrapper(PrintWriter main, PrintWriter branch) {
            super(main, true);
            this.branch = branch;
        }

        @Override
		public void write(char buf[], int off, int len) {
            super.write(buf, off, len);
            super.flush();
            branch.write(buf, off, len);
            branch.flush();
        }

        @Override
		public void write(String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
            branch.write(s, off, len);
            branch.flush();
        }

        @Override
		public void write(int c) {
            super.write(c);
            super.flush();
            branch.write(c);
            branch.flush();
        }

        @Override
		public void flush() {
            super.flush();
            branch.flush();
        }
    }
}
