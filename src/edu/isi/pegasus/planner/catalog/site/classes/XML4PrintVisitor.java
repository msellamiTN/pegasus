/*
 *
 *   Copyright 2007-2008 University Of Southern California
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package edu.isi.pegasus.planner.catalog.site.classes;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * Prints the Site Catalog compatible with Site Catalog schema version 4
 * 
 * https://pegasus.isi.edu/wms/docs/schemas/sc-4.0/sc-4.0.html
 *
 * @author Karan Vahi
 * @version $Revision$
 */
public  class  XML4PrintVisitor implements SiteDataVisitor {

    /**
     * The internal writer
     */
    private Writer mWriter;

    /**
     * The new line character to be used
     */
    private String newLine;

    /**
     * The number of tabs to use for current indent
     */
    private int mCurrentIndentIndex;

   /**
     * Initialize the visitor implementation
     *
     * @param writer  the writer
     */
    public void initialize( Writer writer ){
        mWriter = writer;
        newLine =  System.getProperty( "line.separator", "\r\n" );
        mCurrentIndentIndex = 0;
    }

    /**
     * Visit the SiteStore object
     *
     * @param entry  the site store
     */
    public void visit( SiteStore entry ) throws IOException{
        
    }


    /**
     * Depart  the Site Store object.
     *
     * @param entry  the SiteStore
     */
    public  void depart( SiteStore entry  )throws IOException{

    }


    /**
     * Visit the Site CatalogEntry object
     *
     * @param entry  the site catalog entry
     *
     * @throws IOException in case of problem of writing
     */
    public void visit( SiteCatalogEntry entry ) throws IOException{
        String indent = getCurrentIndent();

        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<site " );
        writeAttribute( mWriter, "handle", entry.getSiteHandle() );
        writeAttribute( mWriter, "arch", entry.getArchitecture().toString() );
        writeAttribute( mWriter, "os", entry.getOS().toString() );

        String val = null;
        if ( ( val = entry.getOSRelease() ) != null ){
            writeAttribute( mWriter, "osrelease", val );
        }

        if ( ( val = entry.getOSVersion() ) != null ){
            writeAttribute( mWriter, "osversion", val );
        }

        if ( ( val = entry.getGlibc() ) != null ){
            writeAttribute( mWriter, "glibc", val );
        }

        mWriter.write( ">");
        mWriter.write( newLine );

        //for our nested elements we have to increment the index
        incrementIndentIndex();

        
    }


    /**
     * Depart  the Site Catalog Entry object.
     *
     * @param entry  the site catalog entry
     */
    public void depart( SiteCatalogEntry entry   ) throws IOException{
        String indent = getCurrentIndent();

        entry.getProfiles().toXML( mWriter, indent );

        closeElement( "site" );
        
    }



    /**
     * Visit the GridGateway object
     *
     * @param gateway  the grid gateway
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void visit( GridGateway gateway) throws IOException{
         String indent = getCurrentIndent();

        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<grid " );
        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<grid " );
        writeAttribute( mWriter, "type", gateway.getType().toString() );
        writeAttribute( mWriter, "contact", gateway.getContact() );
        writeAttribute( mWriter, "scheduler", gateway.getScheduler().toString() );
        writeAttribute( mWriter, "jobtype", gateway.getJobType().toString() );

        if( gateway.getOS() != null ){
            writeAttribute( mWriter, "os", gateway.getOS().toString() );
        }
        if( gateway.getArchitecture() != null ){
            writeAttribute( mWriter, "arch", gateway.getArchitecture().toString() );
        }

        String val = null;
        if ( ( val = gateway.getOSRelease() ) != null ){
            writeAttribute( mWriter, "osrelease", val );
        }

        if ( ( val = gateway.getOSVersion() ) != null ){
            writeAttribute( mWriter, "osversion", val );
        }

        if ( ( val = gateway.getGlibc() ) != null ){
            writeAttribute( mWriter, "glibc", val );
        }

        if( gateway.getIdleNodes() != -1 ){
            writeAttribute( mWriter, "idle-nodes", Integer.toString( gateway.getIdleNodes() ));
        }

        if( gateway.getTotalNodes() != -1 ){
            writeAttribute( mWriter, "total-nodes", Integer.toString( gateway.getTotalNodes() ));
        }


        mWriter.write( ">");
        mWriter.write( newLine );
    }


    /**
     * Depart  the GridGateway object
     *
     * @param entry  GridGateway object
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void depart( GridGateway entry  ) throws IOException{

    }



    /**
     * Visit the directory object
     *
     * @param directory  the directory
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void visit( Directory directory ) throws IOException{
        String indent    = this.getCurrentIndent();

        //sanity check?
        if( directory.isEmpty() ){
            return;
        }

        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<directory " );
        InternalMountPoint imt = directory.getInternalMountPoint();
        if( imt != null ){
            writeAttribute( mWriter, "path", imt.getMountPoint() );
            writeAttribute( mWriter, "type", directory.getType().toString() );

            //take care of optional attributes
            String value = imt.getFreeSize();
            if( value != null ){
                writeAttribute( mWriter, "free-size", value );
            }
            if( (value = imt.getTotalSize()) != null ){
               writeAttribute( mWriter, "total-size", value );
            }

        }


        mWriter.write( ">");
        mWriter.write( newLine );

        //for our nested elements we have to increment the index
        incrementIndentIndex();
    }

    /**
     * Depart the shared directory
     *
     * @param directory  the directory
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void depart( Directory directory ) throws IOException{
        closeElement( "directory" );
    }



        
    /**
     * Visit FileServer site data object
     *
     * @param server  the object corresponding to the FileServer
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void visit( FileServer server )throws IOException{
        String indent    = this.getCurrentIndent();


        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<file-server " );

        //fixed for time being
        writeAttribute( mWriter, "operation", "all" );
        writeAttribute( mWriter, "url", server.getURL() );
        
        mWriter.write( ">");
        mWriter.write( newLine );

        //for our nested elements we have to increment the index
        incrementIndentIndex();
    }

    /**
     * Depart the Directory object
     *
     * @param server  the object corresponding to the FileServer
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void depart( FileServer server) throws IOException{
        String indent = getCurrentIndent();

        server.getProfiles().toXML( mWriter, indent );

        closeElement( "file-server" );

    }

    /**
     * Visit the ReplicaCatalog object
     *
     * @param catalog  the object describing the catalog
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public void visit( ReplicaCatalog catalog  ) throws IOException{
        String indent    = this.getCurrentIndent();

        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<replica-catalog " );

        //fixed for time being
        writeAttribute( mWriter, "type", catalog.getType() );
        writeAttribute( mWriter, "url", catalog.getURL() );

        mWriter.write( ">");
        mWriter.write( newLine );

        //for our nested elements we have to increment the index
        incrementIndentIndex();
    }

    /**
     * Depart the ReplicaCatalog object
     *
     * @param catalog  the object describing the catalog
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public  void depart( ReplicaCatalog catalog  ) throws IOException{
        String indent = getCurrentIndent();

        //list all the aliases first
        for( Iterator<String> it = catalog.getAliasIterator(); it.hasNext(); ){
                catalog.writeAlias( mWriter, indent, it.next() );

        }

        closeElement( "replica-catalog" );
    }

    /**
     * Visit the connection object
     *
     * @param c  the connection.
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public  void visit( Connection c ) throws IOException{
        String indent    = this.getCurrentIndent();

        //write out the  xml element
        //write out the  xml element
        mWriter.write( indent );
        mWriter.write( "<connection " );

        writeAttribute( mWriter, "key", c.getKey() );

        mWriter.write( ">");
        mWriter.write( c.getValue() );
        mWriter.write( "</connection>" );
        mWriter.write( newLine );
        
        //for our nested elements we have to increment the index
        incrementIndentIndex();
    }

    /**
     * Depart the connection object
     *
     * @param c  the connection.
     *
     * @throws  IOException  in case of error while writing to underlying stream
     */
    public  void depart( Connection c  ) throws IOException{
        closeElement( "connection" );
    }
    
    /**
     * Writes an attribute to the stream. Wraps the value in quotes as required
     * by XML.
     *
     * @param writer
     * @param key
     * @param value
     */
    public void writeAttribute( Writer writer, String key, String value ) throws IOException {
        writer.write( " " );
        writer.write( key );
        writer.write( "=\"");
        writer.write( value );
        writer.write( "\"" );
    }

    /**
     * Returns the current indent to be used while writing out
     * 
     * @return  the current indent
     */
    public String getCurrentIndent() {
        StringBuffer indent = new StringBuffer();
        for( int i = 0; i < this.mCurrentIndentIndex ; i++ ){
            indent.append( "\t" );
        }
        return indent.toString();
    }

    /**
     * Returns the indent to be used for the nested element.
     * 
     * @return the new indent
     */
    public String getNextIndent() {
        return this.getCurrentIndent() + "\t";
    }

    /**
     * Increments the indent index
     */
    public void incrementIndentIndex() {
        mCurrentIndentIndex++;
    }


    /**
     * Decrements the indent index
     */
    public void decrementIndentIndex() {
        mCurrentIndentIndex--;
    }


    /**
     * Generates a closing tag for an element
     *
     * @param element  the element tag name
     *
     * @throws IOException
     */
    public void closeElement( String element ) throws IOException{
        //decrement the IndentIndex
        decrementIndentIndex();
        String indent = getCurrentIndent();
        mWriter.write( indent );
        mWriter.write( "</" );
        mWriter.write( element );
        mWriter.write( ">" );
        mWriter.write( newLine );
    }


    public void visit(SiteData data) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void depart(SiteData data) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}