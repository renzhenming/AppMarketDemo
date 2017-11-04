
cmake_minimum_required(VERSION 3.4.1)


add_library(
             compress_image

             SHARED

             src/main/cpp/compress_image.cpp )

find_library(
              log-lib

              log )
find_library(
              jnigraphics-lib

              jnigraphics )


#导入类库,只是作为引用,不编译
add_library( libjpeg
             SHARED
             IMPORTED )
# 指定库的路径
set_target_properties( # Specifies the target library.
                       libjpeg

                       # Specifies the parameter you want to define.
                       PROPERTIES IMPORTED_LOCATION

                       # Provides the path to the library you want to import.
                       ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libjpeg.so )

# 设置头文件的路径
include_directories( src/main/cpp/jpeg )


# 将预构建库与本地库相连
target_link_libraries(
                       compress_image
                       libjpeg
                       ${jnigraphics-lib}
                       ${log-lib} )