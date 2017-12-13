package com.rzm.commonlibrary.general.videoeditor.filter.helper;


import android.content.Context;

import com.rzm.commonlibrary.general.videoeditor.filter.basefilter.GPUImageFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.MagicAntiqueFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.MagicCoolFilter;

public class MagicFilterFactory {

    private static MagicFilterType filterType = MagicFilterType.NONE;

    public static GPUImageFilter initFilters(Context context, MagicFilterType type) {
        if (type == null) {
            return null;
        }
        filterType = type;
        switch (type) {
            case ANTIQUE:
                return new MagicAntiqueFilter(context);
            case COOL:
                return new MagicCoolFilter(context);
            default:
                return null;
        }
    }

    public MagicFilterType getCurrentFilterType() {
        return filterType;
    }


}
