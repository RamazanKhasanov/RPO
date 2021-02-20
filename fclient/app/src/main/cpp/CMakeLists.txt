# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("fclient")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             native-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# add spdlog
add_definitions(-DSPDLOG_COMPILED_LIB)
add_library(spdlog STATIC IMPORTED)
set_target_properties(
        spdlog PROPERTIES IMPORTED_LOCATION C:/Users/xasan/AndroidStudioProjects/libs/spdlog/build/${ANDROID_ABI}/libspdlog.a
)

target_include_directories(
        native-lib PUBLIC
        C:/Users/xasan/AndroidStudioProjects/libs/spdlog/spdlog/include
        C:/Users/xasan/AndroidStudioProjects/libs/mbedtls/mbedtls/include
)

add_library(mbedcrypto SHARED IMPORTED)
set_target_properties(
        mbedcrypto PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/../cmakeLibs/${ANDROID_ABI}/libmbedcrypto.so
)

configure_file(
        C:/Users/xasan/AndroidStudioProjects/libs/mbedtls/build/${ANDROID_ABI}/library/libmbedcrypto.so
        ${CMAKE_CURRENT_SOURCE_DIR}/../cmakeLibs/${ANDROID_ABI}/libmbedcrypto.so
        COPYONLY)
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib
                       spdlog
                       mbedcrypto
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )