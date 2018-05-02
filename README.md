Builds the [Boost C++ Libraries](http://www.boost.org/) for the Android platform, with Google's Ndk.

Tested with **Boost 1.67.0** and **Google's Ndk 16b**  (current versions as of May 2018).



Works with **clang** (llvm) 
*- as of ndk 16 google no longer supports gcc*.

Creates binaries for multiple abis (**armeabi-v7a**, **x86** ...).


*Tested with a development machine running OpenSuse Tumbleweed Linux.*

*Note: Please use the [ndk_15 branch](https://github.com/dec1/Boost-for-Android/edit/ndk_15) if you want to build with the older ndk 15.*


## Usage

* Download and extract the boost source archive to a directory of the form *..../major.minor.patch* 
  eg */home/declan/Documents/zone/mid/lib/boost/1.67.1*
  
  *__Note__:* After the extarction *..../boost/1.67.1* should then be the direct parent dir of "bootstrap.sh", "boost-build.jam" etc

```
> ls /home/declan/Documents/zone/mid/lib/boost/1.67.1
boost  boost-build.jam  boostcpp.jam  boost.css  boost.png  ....
``` 

* Clone this repo:

```
> git clone https://github.com/dec1/Boost-for-Android.git ./boost_for_android
``` 


* Modify the paths (where the ndk is) and variables (which abis you want to build for, which compiler to use etc) in *doIt.sh*, and execute it. If the build succeeds then the boost binaries should then be available in the dir *boost_for_android/build*

```
> cd boost_for_android
> ./doIt.sh
```

* You may need to have *libncurses.so.5* available on you development machine. Install via your os package manager (eg Yast) if necessary.



* *__Note__:* If for some reason the build fails you may want to manually clear the */tmp/ndk-your_username* dir (which gets cleared automatically after a successful build).




## Test App 
Also included is test app which can be opened by Android Studio (see *./test-boost*). If you build and run this app it should show the date and time as calculated by boost *chrono*  (indicating that you have built, linked to and called the boost library correctly).
To use the test app make sure to adjust the values in the  file **local.properties**.

*Note:* The test app uses [CMake for Android](https://developer.android.com/ndk/guides/cmake)


## *Header-only* Boost Libraries
Many of the boost libraries (eg. *algorithm*) can be used as "header only" ie do not require compilation . So you may get away with not building boost if you only
want to use these. To see which of the libraries do require building you can switch to the dir where you extracted the boost download and call:

```
> ./bootstrap.sh --show-libraries 
```

which for example with boost 1.67 produces the output:

```
The Boost libraries requiring separate building and installation are:
    - atomic
    - chrono
    - container
    - context
    - contract
    - coroutine
    - date_time
    - exception
    - fiber
    - filesystem
    - graph
    - graph_parallel
    - iostreams
    - locale
    - log
    - math
    - mpi
    - program_options
    - python
    - random
    - regex
    - serialization
    - signals
    - stacktrace
    - system
    - test
    - thread
    - timer
    - type_erasure
    - wave
```
## Crystax
[Crystax](https://www.crystax.net/) is an excellent alternative to Google's Ndk. It ships with prebuilt boost binaries, and dedicated build scripts.
These binaries will however not work with Goolge's Ndk. If for some reason you can't or don't want to use Crystax then you can't use their boost binaries or build scripts.

## Contributions
- Many thanks to [crystax](https://github.com/crystax/android-platform-ndk/tree/master/build/tools) for their version of *build-boost.sh* which I adapted to make it work with the google ndk.
- Thanks to [google](https://android.googlesource.com/platform/ndk/+/master/build/tools) for the  files *dev-defaults.sh, ndk-common.sh, prebuilt-common.sh*.
- Thanks to [Ryan Pavlik](https://github.com/sensics/Boost-for-Android) for his fork with some improvements.
