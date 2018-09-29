# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)


#-----------------------------------------
#add_library (tuo  SHARED
#             src/main/cpp/tyrant_optimize.cpp)


#-----------------------------------------
add_library (boost_system SHARED IMPORTED)

set_target_properties( boost_system PROPERTIES IMPORTED_LOCATION
                    ${MY_BOOST_LIBS_DIR}/${ANDROID_ABI}/libboost_system.so
                    )
#-----------------------------------------
add_library( boost_chrono SHARED IMPORTED)

set_target_properties(boost_chrono PROPERTIES IMPORTED_LOCATION
                    ${MY_BOOST_LIBS_DIR}/${ANDROID_ABI}/libboost_chrono.so
                    )
#-----------------------------------------
add_library( boost_thread SHARED IMPORTED)

set_target_properties(boost_thread PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_LIBS_DIR}/${ANDROID_ABI}/libboost_thread.so
        )
#-----------------------------------------
add_library( boost_regex SHARED IMPORTED)

set_target_properties(boost_regex PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_LIBS_DIR}/${ANDROID_ABI}/libboost_regex.so
        )
#-----------------------------------------
add_library( boost_timer SHARED IMPORTED)

set_target_properties(boost_timer PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_LIBS_DIR}/${ANDROID_ABI}/libboost_timer.so
        )
#-----------------------------------------
add_library( boost_filesystem SHARED IMPORTED)

set_target_properties(boost_filesystem PROPERTIES IMPORTED_LOCATION
        ${MY_BOOST_LIBS_DIR}/${ANDROID_ABI}/libboost_filesystem.so
        )
#-----------------------------------------

include_directories( ${MY_BOOST_INC_DIR}
                    )
include_directories(src/main/cpp/)
#-----------------------------------------
find_library (log-lib log)

#-----------------------------------------


add_library (tuo  SHARED
        src/main/cpp/tyrant_optimize.cpp
        src/main/cpp/cards.cpp
        src/main/cpp/deck.cpp
        src/main/cpp/read.cpp
        src/main/cpp/sim.cpp
        src/main/cpp/tyrant.cpp
        src/main/cpp/xml.cpp)

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



