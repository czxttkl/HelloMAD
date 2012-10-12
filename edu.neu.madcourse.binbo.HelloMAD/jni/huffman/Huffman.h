/*
 * Huffman.h
 *
 *  Created on: Oct 5, 2012
 *      Author: bigbug
 */

#ifndef HUFFMAN_H_
#define HUFFMAN_H_

#ifdef __cplusplus
extern "C" {
#endif

int compress(char *pszSrcFileName, char *pszObjFileName);
int decompress(char *pszSrcFileName, char *pszObjFileName);
int decompress_v2(FILE* in, int offset, char* buf);

#ifdef __cplusplus
}
#endif

#endif /* HUFFMAN_H_ */
