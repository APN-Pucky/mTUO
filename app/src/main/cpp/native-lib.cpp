#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <sstream>
#include <android/log.h>


#include <boost/chrono.hpp>
#include <boost/lexical_cast.hpp>
#include <boost/timer/timer.hpp>


#define APPNAME "MyApp"

using std::string;

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_declan_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject obj/* this */) {
    boost::timer::auto_cpu_timer autott;
    std::stringstream out;
    //std::streambuf * old = std::cout.rdbuf(buffer1.rdbuf());
    string Str = "Hello from C++";

    //-------------------------------------
    boost::chrono::system_clock::time_point p  = boost::chrono::system_clock::now();
    std::time_t t = boost::chrono::system_clock::to_time_t(p);

    char buffer[26];
    ctime_r(&t, buffer);

    //  std::string tst = std::to_string(3);

    int ver = BOOST_VERSION;
    int ver_maj = ver/100000;
    int ver_min = ver / 100 %1000;
    int ver_pat = ver %100;

    string Ver_Maj = boost::lexical_cast<string>(ver_maj);
    string Ver_Min = boost::lexical_cast<string>(ver_min);
    string Ver_Pat = boost::lexical_cast<string>(ver_pat);
    out << "\n Boost version: " + Ver_Maj + "." + Ver_Min + "." + Ver_Pat + std::endl;
    out  << "... says time is " + std::string(buffer) + "\n\n";
    //--------------------------------------------
    std::string text = out.str();
    jstring jstr = env->NewStringUTF("This string comes from JNI");
    jclass clazz = env->FindClass("com/example/declan/myapplication/MainActivity");
    jmethodID messageMe = env->GetMethodID(clazz, "messageMe", "(Ljava/lang/String;)V");
    env->CallVoidMethod(obj ,messageMe,jstr);
    const char* str = env->GetStringUTFChars(jstr, NULL);
    printf("%s\n",str);
    env->ReleaseStringUTFChars(jstr, str);
    return env->NewStringUTF(text.c_str());

}
