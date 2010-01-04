/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.message.impl;

import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

/**
 * Represents a MIME multipart body (see RFC 2045).A multipart body has a
 * ordered list of body parts. The multipart body also has a preamble and
 * epilogue. The preamble consists of whatever characters appear before the
 * first body part while the epilogue consists of whatever characters come after
 * the last body part.
 */
public class MultipartImpl extends Multipart {

    private ByteSequence preamble;
    private transient String preambleStrCache;
    private ByteSequence epilogue;
    private transient String epilogueStrCache;

    /**
     * Creates a new empty <code>Multipart</code> instance.
     */
    public MultipartImpl(String subType) {
        super(subType);
        preamble = ByteSequence.EMPTY;
        preambleStrCache = "";
        epilogue = ByteSequence.EMPTY;
        epilogueStrCache = "";
    }

    /**
     * Creates a new <code>Multipart</code> from the specified
     * <code>Multipart</code>. The <code>Multipart</code> instance is
     * initialized with copies of preamble, epilogue, sub type and the list of
     * body parts of the specified <code>Multipart</code>. The parent entity
     * of the new multipart is <code>null</code>.
     * 
     * @param other
     *            multipart to copy.
     * @throws UnsupportedOperationException
     *             if <code>other</code> contains a {@link SingleBody} that
     *             does not support the {@link SingleBody#copy() copy()}
     *             operation.
     * @throws IllegalArgumentException
     *             if <code>other</code> contains a <code>Body</code> that
     *             is neither a {@link Message}, {@link Multipart} or
     *             {@link SingleBody}.
     */
    public MultipartImpl(Multipart other) {
    	super(other.getSubType());

    	for (Entity otherBodyPart : other.getBodyParts()) {
    		Entity bodyPartCopy = new BodyPart(otherBodyPart);
            addBodyPart(bodyPartCopy);
        }

    	if (other instanceof MultipartImpl) {
	        preamble = ((MultipartImpl) other).preamble;
	        epilogue = ((MultipartImpl) other).epilogue;
	        preambleStrCache = ((MultipartImpl) other).preambleStrCache;
	        epilogueStrCache = ((MultipartImpl) other).epilogueStrCache;
    	} else {
    		setPreamble(other.getPreamble());
    		setEpilogue(other.getEpilogue());
    	}
    }

    // package private for now; might become public someday
    public ByteSequence getPreambleRaw() {
        return preamble;
    }

    public void setPreambleRaw(ByteSequence preamble) {
        this.preamble = preamble;
        this.preambleStrCache = null;
    }

    /**
     * Gets the preamble.
     * 
     * @return the preamble.
     */
    public String getPreamble() {
        if (preambleStrCache == null) {
            preambleStrCache = ContentUtil.decode(preamble);
        }
        return preambleStrCache;
    }

    /**
     * Sets the preamble.
     * 
     * @param preamble
     *            the preamble.
     */
    public void setPreamble(String preamble) {
        this.preamble = ContentUtil.encode(preamble);
        this.preambleStrCache = preamble;
    }

    // package private for now; might become public someday
    public ByteSequence getEpilogueRaw() {
        return epilogue;
    }

    public void setEpilogueRaw(ByteSequence epilogue) {
        this.epilogue = epilogue;
        this.epilogueStrCache = null;
    }

    /**
     * Gets the epilogue.
     * 
     * @return the epilogue.
     */
    public String getEpilogue() {
        if (epilogueStrCache == null) {
            epilogueStrCache = ContentUtil.decode(epilogue);
        }
        return epilogueStrCache;
    }

    /**
     * Sets the epilogue.
     * 
     * @param epilogue
     *            the epilogue.
     */
    public void setEpilogue(String epilogue) {
        this.epilogue = ContentUtil.encode(epilogue);
        this.epilogueStrCache = epilogue;
    }

}