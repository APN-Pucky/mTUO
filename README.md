# Boost for Android


Builds the [Boost C++ Libraries](http://www.boost.org/) for the Android platform, with Google's Ndk.

Tested with **Boost 1.65.1** and **Google's Ndk 15c**  (current versions as of Sept 2017)

[Crystax](https://www.crystax.net/) is an excellent alternative to Google's Ndk. It ships with prebuilt boost binaries, and dedicated build scripts.
These binaries will however not work with Goolge's Ndk. If for some reason you can't or don't want to use Crystax then you can't use their boost binaries or build scripts.

This bash script is based on the Crystax build script but modified so that it will build boost with the Google Ndk.


Works with **gcc** and **clang** (llvm)
Creates binaries for multiple abis (**armeabi-v7a**, **x86**, mips etc).


*Tested with a development machine running OpenSuse Tumbleweed Linux.*

## Usage

* Download and extract the boost source archive to a directory of the form *..../major.minor.patch* 
  eg */home/declan/Documents/zone/mid/lib/boost/1.65.1*
  
  *__Note__:* After the extarction *..../boost/1.65.1* should then be the direct parent dir of "bootstrap.sh", "boost-build.jam" etc

```
> ls /home/declan/Documents/zone/mid/lib/boost/1.65.1
boost  boost-build.jam  boostcpp.jam  boost.css  boost.png  ....
``` 

* Create a symbolic link *llvm-3.5* to *llvm* in the *toolchains* subdir of the ndk *(This is only necessary if you want to be able to build with clang)*

eg
```
cd path_to_ndk/toolchains
ln -s llvm llvm-3.5
```

* You may need to have *libncurses.so.5* available on you development machine. Install via your os package manager (eg Yast) if necessary.

* Modify the paths (where the ndk is) and variables (which abis you want to build for, which compiler to use etc) in *doIt.sh*, and execute it.

* *__Note__:* If for some reason the build fails you may want to manually clear the */tmp/ndk-your_username* dir (which gets cleared automatically after a successful build).




## Test App 
Also included is test app which can be opened by Android Studio (see ./test-boost).
To use the test app make sure to adjust the paths in local.properties (see local.properties_example).
Note: The test app uses [Gradle Experimental](http://tools.android.com/tech-docs/new-build-system/gradle-experimental)





