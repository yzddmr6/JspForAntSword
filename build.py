import os
import sys
import base64
import time
'''
usage: python3 build.py
替换你的javac路径跟lib路径后在当前目录下运行,即可完成对当前路径下所有.java文件的编译以及生成base64格式的payload
code by yzddmr6
'''

javapath = 'C:/Java/jdk1.6.0_43/bin/javac.exe' #javac路径
classpath = "D:/apache-tomcat-7.0.105/lib"  #apache lib路径
classpath = classpath+"/servlet-api.jar;"+classpath+"/jsp-api.jar"  #拼接classpath
def genpayload(targetfile):
    res = os.popen(f'"{javapath}" -cp {classpath} {targetfile}')
    time.sleep(0.5)
    targetclass = targetfile.replace('.java', '.class')
    if os.path.exists(targetclass):
        with open(targetclass, 'rb') as f:
            content = f.read()
        res = str(base64.b64encode(content),"UTF-8")
    # print(str(res,"UTF-8"))
    targettxt=targetfile.replace('.java','.txt')

    with open(targettxt,'w') as save:
        save.write(res)
    if save:
        print(targetfile+" Build Success!")
    else:
        print(targetfile+" Build Failed!")

# genpayload(sys.argv[1])

for root,dirs,files in os.walk('.'):
    for f in files:
        if f.endswith(".java"):
            path=os.path.join(root,f)
            print('------------------------------------------------------------')
            print(path)
            genpayload(path)

