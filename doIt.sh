# Copyright (c) 2017 Declan Moran (www.silverglint.com)

# Extract boost (src) archive to a directory of the form "major.minor.patch" 
# so that the dir name ~  boost version (eg "1.64.0")
#---------------------------------------------------------------------------------
# Example script. Modify the variables below as appropriate for your local setup.
#---------------------------------------------------------------------------------





# Specify the path to boost source code dir 
BOOST_SRC_DIR=/home/declan/Documents/zone/low/Boost-for-Android/src

#------------------------------------------------------------------------------------------
# Specify the version of boost youre building
#BOOST_VERSION=1.64.0
BOOST_VERSION=1.67.0

#------------------------------------------------------------------------------------------
# Specify path to the (Google) Ndk  (by default  downloded to "..sdk/ndk-bundle" by android studio)
#ANDROID_NDK_ROOT=/home/declan/Documents/zone/mid/lib/android/ndk/15c/android-ndk-r15c
export ANDROID_NDK_ROOT=/home/declan/Documents/zone/mid/lib/android/sdk/ndk-bundle

#------------------------------------------------------------------------------------------
# Modify if desired
# log file where build messages will be stored
logFile=build_out.txt
rm $logFile

#------------------------------------------------------------------------------------------
# the build script writes some extra info to this file if its defined (but will be in "split" across different dirs)
#export NDK_LOGFILE=ndk_log_out.txt



# The options --stdlibs, --abis and --linkage can be one or more of the listed posible values. If you specify more than one, then separate individual values by a comma
#------------------------------------------------------------------------------------------
# which compiler to use       // gnu-4.9 removed as of ndk 16
STD_LIBS="llvm"
  

# which abis (~ architecture + instruction set) to build for     
# possible values:  {armeabi-v7a,arm64-v8a,x86,x86_64}
#ABIS="armeabi-v7a"
ABIS="armeabi-v7a,arm64-v8a,x86,x86_64"

# whether to build shared or static libraries (or both)          
# possible values:   {shared, static}
LINKAGE="shared"               
#LINKAGE="shared, static"



#--------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------
# Dont modify  - the actual call
#-------------------------------

./build_tools/build-boost.sh --version=$BOOST_VERSION --stdlibs=$STD_LIBS --abis=$ABIS  --ndk-dir=$ANDROID_NDK_ROOT --linkage=$LINKAGE --verbose $BOOST_SRC_DIR  2>&1 | tee -a $logFile




