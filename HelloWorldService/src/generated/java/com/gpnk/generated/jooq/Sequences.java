/*
 * This file is generated by jOOQ.
 */
package com.gpnk.generated.jooq;


import javax.annotation.processing.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.locations_location_id_seq</code>
     */
    public static final Sequence<Integer> LOCATIONS_LOCATION_ID_SEQ = new SequenceImpl<Integer>("locations_location_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.INTEGER.nullable(false));

    /**
     * The sequence <code>public.users_user_id_seq</code>
     */
    public static final Sequence<Integer> USERS_USER_ID_SEQ = new SequenceImpl<Integer>("users_user_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.INTEGER.nullable(false));
}
