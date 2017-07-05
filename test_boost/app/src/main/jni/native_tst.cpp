
#include <string>
#include <jni.h>

#include <boost/chrono/chrono.hpp>
#include <boost/lexical_cast.hpp>
using std::string;

//----------------------------------------------------------------------
//----------------------------------------------------------------------


extern "C"
{

    JNIEXPORT jstring JNICALL
    Java_com_testboost_JcNativeMan_cppInfoStr(JNIEnv *Env, jclass Cls, jstring StrIn, int num)
    {
        string Str("hi from C++, boost should give some meaningful info here \:");


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

        Str += "\n Boost version: " + Ver_Maj + "." + Ver_Min + "." + Ver_Pat + "\n";
        Str += "... says time is " + std::string(buffer) + "\n\n";


        return Env->NewStringUTF(Str.c_str());
    }



}
//------------------------------------------------------------------------
//----------------------------------------------------------------------
