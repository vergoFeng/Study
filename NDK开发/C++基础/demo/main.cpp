#include<iostream>
#include "func.h"
using namespace std;

// void* 参数是指针，指针类型不需要关心 
int* addFunc(void* param) {
	cout << "指针函数" << endl;
	int a = 10;
	int *b = &a;
	return b;
}

int main() {
	int a = 10; 
	addFunc(&a);
	return 0;
} 

int func(){
	printf("函数");
	return -1;
}

// 函数重载 
int func(int num) {
	printf("函数");
	return -1;
}

