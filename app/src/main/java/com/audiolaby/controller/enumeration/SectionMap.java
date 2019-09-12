package com.audiolaby.controller.enumeration;

import android.content.Context;

import com.audiolaby.R;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionMap {

    public static String getSectionTitle(SectionType sectionType, Context context) {

        try {
            switch (sectionType) {
                case NEW_POSTS:
                    return context.getString(R.string.section_new_post);

                case TOP_POSTS:
                    return context.getString(R.string.section_popular);

                case POPULAR:
                    return context.getString(R.string.section_top_post);

                case TOP_VOICE:
                    return context.getString(R.string.section_top_voice);

                case FEATURED:
                    return context.getString(R.string.section_featured);

                case FREE:
                    return context.getString(R.string.section_free);

                case PAID:
                    return context.getString(R.string.section_paid);

                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    public static String[]  getSectionType(SectionType sectionType) {

        try {
            switch (sectionType) {
                case NEW_POSTS:
                    return new String[] { TypeField.all.name(), SortField.RELEASE_DATE_DESC.name() };

                case TOP_POSTS:
                    return new String[] { TypeField.all.name(), SortField.HIGHEST_RATED.name() };

                case POPULAR:
                    return new String[] { TypeField.all.name(), SortField.MOST_POPULAR.name() };

                case FEATURED:
                    return new String[] { TypeField.featured.name(), SortField.RELEASE_DATE_DESC.name() };

                case FREE:
                    return new String[] { TypeField.free.name(), SortField.RELEASE_DATE_DESC.name() };

                case PAID:
                    return new String[] { TypeField.paid .name(), SortField.RELEASE_DATE_DESC.name() };

                default:
                    return new String[] { TypeField.all.name(), SortField.RELEASE_DATE_DESC.name() };
            }
        } catch (Exception e) {
            return new String[] { TypeField.all.name(), SortField.RELEASE_DATE_DESC.name() };
        }

    }

}
