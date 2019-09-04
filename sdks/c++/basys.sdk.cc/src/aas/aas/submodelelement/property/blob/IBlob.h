/*
 * IBlob.h
 *
 *      Author: wendel
 */ 

#ifndef BASYX_METAMODEL_IBlob_H_
#define BASYX_METAMODEL_IBlob_H_


#include "BlobType.h"
#include "MimeType.h"

class IBlob
{
public:
	virtual ~IBlob() = default;

	virtual void setValue(const BlobType & value) = 0;
	virtual BlobType getValue() const = 0;

	virtual void setMimeType(const MimeType & mimeType) = 0;
	virtual MimeType getMimeType() const = 0;
};

#endif

