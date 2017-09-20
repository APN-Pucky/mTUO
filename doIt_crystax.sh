# Copyright (c) 2017 Declan Moran (www.silverglint.com)

# Extract boost (src) archive to a directory of the form "major.minor.patch" 
# so that the dir name ~  boost version (eg "1.64.0")


# Example script. Modify the variables below as appropriate for your local setup.
#---------------------------------------------------------------------------------

# Specify the path to boost src dir 
BOOST_SRC_DIR=/home/declan/Documents/zone/mid/lib/boost

# Specify the version of boost in the source dir
BOOST_VERSION=1.64.0

# Specify path to (Google) Ndk
CRYSTAX_DIR=/home/declan/Documents/zone/mid/lib/android/crystax/stable


# Dont modify
export ANDROID_NDK_ROOT=$CRYSTAX_DIR

# Modify if desired
# log file where build messages will be stored
logFile=build_out.txt
rm $logFile


# the build script writes some extra info to this file if its defined (but will be in "split" across different dirs)
#export NDK_LOGFILE=ndk_log_out.txt



$CRYSTAX_DIR/build/tools/build-boost.sh --version=1.64.0 --stdlibs="gnu-4.9, gnu-5, llvm-3.6, llvm-3.7" --abis="armeabi-v7a, x86"  --ndk-dir=$ANDROID_NDK_ROOT --verbose $BOOST_SRC_DIR  2>&1 | tee -a $logFile 
# ./build_tools/build-boost.sh --version=1.64.0 --stdlibs="gnu-4.9" --abis="armeabi-v7a"  --ndk-dir=$ANDROID_NDK_ROOT --linkage="shared" --verbose $BOOST_SRC_DIR  2>&1 | tee -a $logFile


# gets installed to stable/sources/boost/$BOOST_VERSION
