/*************************************************************************
 * tranSMART - translational medicine data mart
 * 
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 * 
 * This product includes software developed at Janssen Research & Development, LLC.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
 * 1.	You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
 * 2.	You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 *
 ******************************************************************/
  

package org.transmart.biomart
class ContentReference {
		Long id
		String type
		Content content
		Long bioDataId
 static mapping = {
	 table 'BIO_CONTENT_REFERENCE'
	 version false
	cache usage:'read-only'
	 id generator:'sequence', params:[sequence:'SEQ_BIO_DATA_ID', schema: 'biomart']
	 columns {
		id column:'BIO_CONTENT_REFERENCE_ID'
		type column:'CONTENT_REFERENCE_TYPE'
		content column:'BIO_CONTENT_ID'
		bioDataId column:'BIO_DATA_ID'
		}
	}

		static constraints = {
			type(maxSize:400)
		}

}
