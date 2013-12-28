//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package com.dyuproject.protostuff;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * An output used for writing data with xml format.
 *
 * @author David Yu
 * @created May 24, 2010
 */
public final class XmlOutput implements Output, StatefulOutput
{
    
    private final XMLStreamWriter writer;
    private Schema<?> schema;
    
    public XmlOutput(XMLStreamWriter writer)
    {
        this(writer, null);
    }
    
    public XmlOutput(XMLStreamWriter writer, Schema<?> schema)
    {
        this.writer = writer;
        this.schema = schema;
    }
    
    public XmlOutput use(Schema<?> schema)
    {
        this.schema = schema;
        return this;
    }
    
    public void updateLast(Schema<?> schema, Schema<?> lastSchema)
    {
        if(lastSchema != null && lastSchema == this.schema)
        {
            this.schema = schema;
        }
    }
    
    private static void write(final XMLStreamWriter writer, final String name, 
            final String value) throws IOException
    {
        try
        {
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
    }
    
    private static void writeB64Encoded(final XMLStreamWriter writer, final String name, 
            final char[] value) throws IOException
    {
        try
        {
            writer.writeStartElement(name);
            
            if(value != null)
                writer.writeCharacters(value, 0, value.length);
            
            writer.writeEndElement();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
    }
    
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }
    
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }
    
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Float.toString(value));
    }
    
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Double.toString(value));
    }
    
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), String.valueOf(value));
    }
    
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }
    
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        write(writer, schema.getFieldName(fieldNumber), value);
    }
    
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }
    
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        writeB64Encoded(writer, schema.getFieldName(fieldNumber), 
                value.length == 0 ? null : B64Code.cencode(value));
    }
    
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, 
            int offset, int length, boolean repeated) throws IOException
    {
        if(utf8String)
        {
            writeString(fieldNumber, STRING.deser(value, offset, length), repeated);
        }
        else
        {
            writeB64Encoded(writer, schema.getFieldName(fieldNumber), 
                    length == 0 ? null : B64Code.cencode(value, offset, length));
        }
    }
    
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema, 
            final boolean repeated) throws IOException
    {
        final Schema<?> lastSchema = this.schema;
        this.schema = schema;
        
        final XMLStreamWriter writer = this.writer;
        try
        {
            writer.writeStartElement(lastSchema.getFieldName(fieldNumber));
            
            schema.writeTo(this, value);
            
            writer.writeEndElement();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
        
        // restore state
        this.schema = lastSchema;
    }

}
