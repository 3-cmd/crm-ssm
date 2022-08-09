package com.cs.crm.commons;

import com.cs.crm.workbench.domain.Activity;
import lombok.Data;

import java.io.Serializable;
@Data
public class ActivityPage extends Activity implements Serializable {
    private Integer pageNumber;
    private Integer pageSize;
}
