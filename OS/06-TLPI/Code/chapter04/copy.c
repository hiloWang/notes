/*
 ============================================================================
 
 Author      : ztiany
 Date        : 19-7-19
 Description : copy 实现

 ============================================================================
 */

#include <fcntl.h>
#include <sys/stat.h>
#include "tlpi_hdr.h"

#ifndef BUF_SIZE
#define BUF_SIZE 1024
#endif

int main(int argc, char *argv[]) {
  int inputFd, outputFd;

  // check args
  if (argc != 3 || strcmp(argv[1], "--help") == 0) {
    usageErr("%s old-file new-file\n", argv[0]);
  }

  for (int i = 0; i < argc; i++) {
    printf("argv %d = %s \n", i, argv[i]);
  }

  // open file
  if ((inputFd = open(argv[1], O_RDONLY)) == -1) {
    err_exit("opening file %s", argv[1]);
  }

  // crate and write and trunc
  int flag = O_CREAT | O_WRONLY | O_TRUNC;
  /*rw-rw-rw-*/
  mode_t perms = S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH;

  if ((outputFd = open(argv[2], flag, perms)) == -1) {
    err_exit("opening file %s", argv[2]);
  }

  char buf[BUF_SIZE];
  ssize_t count;

  while ((count = read(inputFd, buf, BUF_SIZE)) > 0) {
    printf("read count %zd \n", count);
    if (write(outputFd, buf, count) != count) {
      fatal("could not write whole buffer");
    }
  }

  // returning -1 means error
  if (count == -1) {
    errExit("read");
  }

  if (close(inputFd) == -1) {
    errExit("close %s", argv[1]);
  }

  if (close(outputFd) == -1) {
    errExit("close %s", argv[2]);
  }

  printf("copy successfully\n");

  exit(EXIT_SUCCESS);
}