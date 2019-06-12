/*
 ============================================================================
 
 Description : 文件流操作

 ============================================================================
 */

#include <cstdlib>
#include <fstream>
#include <iostream>

using namespace std;

void writeFile(const string file);
void readFile(const string file);
void copy(const string file);

int main() {
    writeFile("02.txt");
    readFile("02.txt");
    copy("02.txt");
    return EXIT_SUCCESS;
}

//将数据从一文件复制到另一文件中
void copy(const string file) {
    ifstream infile;
    ofstream outfile;
    infile.open(file);
    outfile.open("copy_" + file);
    cout << "copy from " << file << " to copy_" << file << endl;
    //rdbuf()可以实现一个流对象指向的内容用另一个流对象来输出
    outfile << infile.rdbuf();
    infile.close();
    outfile.close();
}

void writeFile(const string file) {
    ofstream ofStream(file);
    ofStream << "Hello World";
    ofStream << " ";
    for (int i = 0; i < 300; ++i) {
        ofStream << i;
    }
    ofStream.close();
}

void readFile(const string file) {
    ifstream ifStream(file);
    string content;
    ifStream >> content;//每次读取一个单词，以空格为标识
    cout << content;
    ifStream >> content;//每次读取一个单词
    cout << content << endl;
    ifStream >> content;//每次读取一个单词
    cout << content << endl;
    ifStream.close();
}