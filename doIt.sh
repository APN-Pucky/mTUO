# Copyright (c) 2017 Declan Moran (www.silverglint.com)

# Extract boost (src) archive to a directory of the form "major.minor.patch" 
# so that the dir name ~  boost version (eg "1.64.0")


# Example script. Modify the variables below as appropriate for your local setup.
#---------------------------------------------------------------------------------

# Specify the path to boost src dir 
BOOST_SRC_DIR=/home/declan/Documents/zone/mid/lib/boost/down

# Specify the version of boost in the source dir
#BOOST_VERSION=1.64.0
BOOST_VERSION=1.65.1

# Specify path to (Google) Ndk
#GOOGLE_DIR=/home/declan/Documents/zone/mid/lib/android/sdk/ndk-bundle
#GOOGLE_DIR=/home/declan/Documents/zone/mid/lib/android/ndk/15c/android-ndk-r15c
GOOGLE_DIR=/home/declan/Documents/zone/mid/lib/android/ndk/16b1/android-ndk-r16-beta1


# Dont modify
export ANDROID_NDK_ROOT=$GOOGLE_DIR

# Modify if desired
# log file where build messages will be stored
logFile=build_out.txt
rm $logFile


# the build script writes some extra info to this file if its defined (but will be in "split" across different dirs)
#export NDK_LOGFILE=ndk_log_out.txt


./build_tools/build-boost.sh --version=1.65.1 --stdlibs="gnu-4.9" --abis="armeabi-v7a"  --ndk-dir=$ANDROID_NDK_ROOT --linkage="shared" --verbose $BOOST_SRC_DIR  2>&1 | tee -a $logFile

#./build_tools/build-boost.sh --version=1.65.1 --stdlibs="gnu-4.9, llvm-3.5" --abis="armeabi-v7a, x86"  --ndk-dir=$ANDROID_NDK_ROOT --linkage="shared,static" --verbose $BOOST_SRC_DIR  2>&1 | tee -a $logFile 
# ./build_tools/build-boost.sh --version=1.64.0 --stdlibs="gnu-4.9" --abis="armeabi-v7a"  --ndk-dir=$ANDROID_NDK_ROOT --linkage="shared" --verbose $BOOST_SRC_DIR  2>&1 | tee -a $logFile



