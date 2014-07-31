#include <jni.h>
#include <string>
#include <stdio.h>
#include <sstream>
#include "shapelib/shapefil.h"
#include "CommonUtils.h"
#include "com_example_shapelibtest_ShapLibTest.h"

using namespace std;

void logShapType(int);
string getFieldTypeStr(DBFFieldType);
string getShapeVerticesStr(double*, double*, int);

JNIEXPORT void JNICALL Java_com_example_shapelibtest_ShapLibTest_testRead
  (JNIEnv *env, jclass clazz, jstring jpath) {
	char strBuff[2048];
	memset(strBuff, 0, sizeof(strBuff));
	const char *path = env->GetStringUTFChars(jpath, 0);
	LOGI(path);
	//shp 文件路径不需要带文件名扩展 内部会处理
	SHPHandle hSHP = SHPOpen(path, "r");
	if(hSHP == NULL) {
		LOGI("testRead hSHP == NULL");
		return;
	}
	int nEntities = 0;
	int nShapeType = SHPT_NULL;
	SHPGetInfo(hSHP, &nEntities, &nShapeType, NULL, NULL);
	logShapType(nShapeType);
	sprintf(strBuff, "testRead nEntities=%d", nEntities);
	LOGI(strBuff);
	//dbf 文件路径不需要带文件名扩展 内部会处理
	DBFHandle hDBF = DBFOpen(path, "r");
	if(hDBF == NULL) {
		LOGI("testRead hDBF == NULL");
		return;
	}
	int nFieldCount = DBFGetFieldCount(hDBF);
	sprintf(strBuff, "testRead nFieldCount=%d", nFieldCount);
	LOGI(strBuff);
	int nRecordCount = DBFGetRecordCount(hDBF);
	sprintf(strBuff, "testRead nRecordCount=%d", nRecordCount);
	LOGI(strBuff);
	char (*fieldName)[64] = new char[nFieldCount][64];
	memset(fieldName, 0, sizeof(fieldName));
	DBFFieldType* aDBFFieldType = new DBFFieldType[nFieldCount];
	int nWidth;
	int nDecimals;
	for(int i = 0; i < nFieldCount; ++i) {
		aDBFFieldType[i] = DBFGetFieldInfo(hDBF, i, fieldName[i], &nWidth, &nDecimals);
		sprintf(strBuff, "testRead fieldName[%d]=%s type=%s nWidth=%d nDecimals=%f", i, fieldName[i], getFieldTypeStr(aDBFFieldType[i]).c_str(), nWidth, nDecimals);
		LOGI(strBuff);
	}
	//循环读取shape与attr
	if(nShapeType == SHPT_POLYGON) {
		//string s;
		stringstream ss;
		for(int i = 0; i < nEntities && i < 500; ++i) {
			//read shape
			SHPObject* pSHPObject = SHPReadObject(hSHP, i);
			if(pSHPObject == NULL) {
				sprintf(strBuff, "==================\ntestRead index=%d pSHPObject=NULL", i);
				LOGI(strBuff);

			} else {
				sprintf(strBuff, "==================\ntestRead index=%d pSHPObject: nVertices=%d nShapeId=%d", i, pSHPObject->nVertices, pSHPObject->nShapeId);
				LOGI(strBuff);
				LOGI(("testRead pSHPObject: shape:" + getShapeVerticesStr(pSHPObject->padfX, pSHPObject->padfY, pSHPObject->nVertices)).c_str());
				SHPDestroyObject(pSHPObject);
			}
			//read field
			ss.clear();
			for (int j = 0; j < nFieldCount; ++j) {
				switch (aDBFFieldType[i]) {
				case FTString:
					ss << DBFReadStringAttribute(hDBF, i, j);
					break;
				case FTInteger:
					ss << DBFReadIntegerAttribute(hDBF, i, j);
					break;
				case FTDouble:
					ss << DBFReadDoubleAttribute(hDBF, i, j);
					break;
				case FTLogical:
					ss << "FTLogical";
					break;
				case FTInvalid:
					ss << "FTInvalid";
					break;
				}
				ss << ", ";
			}
			LOGI(("testRead fields:" + ss.str()).c_str());
		}
	}
	delete[] fieldName;
	delete[] aDBFFieldType;
	SHPClose(hSHP);
	DBFClose(hDBF);
	LOGI("testRead end");
}

JNIEXPORT void JNICALL Java_com_example_shapelibtest_ShapLibTest_test
  (JNIEnv *env, jclass clazz, jstring jsdpath, jstring jesdpath) {
	LOGI("test");
	const char *sdpath = env->GetStringUTFChars(jsdpath, 0);
	const char *esdpath = env->GetStringUTFChars(jesdpath, 0);
	LOGI(sdpath);
	FILE* sdfp = fopen(sdpath, "r");
	if(sdfp == NULL) {
		LOGI("sdfp == NULL");
	} else {
		LOGI("sdfp != NULL");
		fclose(sdfp);
	}
	LOGI(esdpath);
	FILE* esdfp = fopen(esdpath, "r");
	if(esdfp == NULL) {
		LOGI("esdfp == NULL");
	} else {
		LOGI("esdfp != NULL");
		fclose(esdfp);
	}
	LOGI("test end");
}

void logShapType(int nShapType) {
	string type = "logShapType type=";
	switch (nShapType) {
	case SHPT_NULL:
		type += "SHPT_NULL";
		break;
	case SHPT_POINT:
		type += "SHPT_POINT";
		break;
	case SHPT_ARC:
		type += "SHPT_ARC";
		break;
	case SHPT_POLYGON:
		type += "SHPT_POLYGON";
		break;
	case SHPT_MULTIPOINT:
		type += "SHPT_MULTIPOINT";
		break;
	default:
		type += nShapType;
		break;
	}
	LOGI(type.c_str());
}

string getFieldTypeStr(DBFFieldType type) {
	switch(type) {
	case FTString:
		return "FTString";
	case FTInteger:
		return "FTInteger";
	case FTDouble:
		return "FTDouble";
	case  FTLogical:
		return "FTLogical";
	case FTInvalid:
		return "FTInvalid";
	}
}

string getShapeVerticesStr(double *padfXint, double	*padfY, int len) {
	stringstream s;
	for(int i = 0; i < len && i < 50; ++i) {
		s<<padfXint[i];
		s<<", ";
		s<<padfY[i];
		s<<", ";
	}
	return s.str();
}



