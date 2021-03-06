/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table;


import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TableRowData {
    private Object rowData;
    private List<Object> cellDatas;
    private List<String> cellStrings;

    public TableRowData(Object rowData, List<Object> cellDatas, List<String> cellStrings) {
        this.rowData = rowData;
        this.cellDatas = Collections.unmodifiableList(cellDatas);
        this.cellStrings = Collections.unmodifiableList(cellStrings);
    }

    public Object getRowData() {
        return rowData;
    }

    public List<Object> getCellDatas() {
        return cellDatas;
    }

    public List<String> getCellStrings() {
        return cellStrings;
    }
}
