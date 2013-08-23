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
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 ******************************************************************/
  

package transmart

import au.com.bytecode.opencsv.CSVReader

class CSVService {

    boolean transactional = false
    private int TO_LAST_ROW = -1

    def csvToJson(URL resource, String separator = ',', fromRow = 1, maxRows = null, useFields = null) {
        assert resource != null
        assert separator && separator.length() == 1

        int toRow = maxRows ? fromRow + maxRows : TO_LAST_ROW

        def rowList = new ArrayList<String[]>()
        def csvReader = new CSVReader(new BufferedReader(new InputStreamReader(resource.openStream(), 'UTF-8')), (char) separator)
        try {
            String[] headerRow
            if((headerRow = csvReader.readNext()) != null) {
                def fields = useFields ? headerRow.intersect(useFields) : headerRow
                String[] row
                int rowNumber = 1
                while ((row = csvReader.readNext()) != null) {
                    if(rowNumber >= fromRow && (toRow == TO_LAST_ROW || rowNumber <= toRow)) {
                        rowList.add(row)
                    }
                    rowNumber += 1
                }
            }
        } finally {
            csvReader.close()
        }
        return rowList
    }

}
