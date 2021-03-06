# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set( CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}  -Ofast")

add_definitions(-DNDEBUG -DNQUEST -DNTIMER)
#-----------------------------------------
#add_library (tuo  SHARED
#             src/main/cpp/tyrant_optimize.cpp)


#-----------------------------------------
add_library (boost_system STATIC IMPORTED)

set_target_properties( boost_system PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_DIR}/${ANDROID_ABI}/lib/libboost_system.a
                    )
#-----------------------------------------
add_library( boost_chrono STATIC IMPORTED)

set_target_properties(boost_chrono PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_DIR}/${ANDROID_ABI}/lib/libboost_chrono.a
                    )
#-----------------------------------------
add_library( boost_thread STATIC IMPORTED)

set_target_properties(boost_thread PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_DIR}/${ANDROID_ABI}/lib/libboost_thread.a
        )
#-----------------------------------------
add_library( boost_regex STATIC IMPORTED)

set_target_properties(boost_regex PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_DIR}/${ANDROID_ABI}/lib/libboost_regex.a
        )
#-----------------------------------------
add_library( boost_timer STATIC IMPORTED)

set_target_properties(boost_timer PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_DIR}/${ANDROID_ABI}/lib/libboost_timer.a
        )
#-----------------------------------------
add_library( boost_filesystem STATIC IMPORTED)

set_target_properties(boost_filesystem PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_DIR}/${ANDROID_ABI}/lib/libboost_filesystem.a
        )
#-----------------------------------------
#include_directories( ${MY_BOOST_DIR_INC} )

include_directories( ${MY_BOOST_DIR}/${ANDROID_ABI}/include/boost-1_68/ )
#-----------------------------------------
find_library (log-lib log)

#-----------------------------------------


add_library (tuo  SHARED
        src/main/cpp/tyrant_optimize/tyrant_optimize.cpp
        src/main/cpp/tyrant_optimize/cards.cpp
        src/main/cpp/tyrant_optimize/deck.cpp
        src/main/cpp/tyrant_optimize/read.cpp
        src/main/cpp/tyrant_optimize/sim.cpp
        src/main/cpp/tyrant_optimize/tyrant.cpp
        src/main/cpp/tyrant_optimize/xml.cpp)

target_link_libraries( # Specifies the target library.
        tuo

        ${log-lib}

        boost_system
        boost_chrono
        boost_thread
        boost_regex
        boost_timer
        boost_filesystem

        )



