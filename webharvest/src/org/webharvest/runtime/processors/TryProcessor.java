/*  Copyright (c) 2006-2007, Vladimir Nikic
    All rights reserved.

    Redistribution and use of this software in source and binary forms,
    with or without modification, are permitted provided that the following
    conditions are met:

    * Redistributions of source code must retain the above
      copyright notice, this list of conditions and the
      following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the
      following disclaimer in the documentation and/or other
      materials provided with the distribution.

    * The name of Web-Harvest may not be used to endorse or promote
      products derived from this software without specific prior
      written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.

    You can contact Vladimir Nikic by sending e-mail to
    nikic_vladimir@yahoo.com. Please include the word "Web-Harvest" in the
    subject line.
*/
package org.webharvest.runtime.processors;

import org.webharvest.definition.BaseElementDef;
import org.webharvest.definition.TryDef;
import org.webharvest.exception.BaseException;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;

/**
 * OnError processor - sets .
 */
public class TryProcessor extends BaseProcessor {

    private TryDef tryDef;

    public TryProcessor(TryDef tryDef) {
        super(tryDef);
        this.tryDef = tryDef;
    }

    public Variable execute(Scraper scraper, ScraperContext context) {
        try {
            BaseElementDef tryBodyDef = tryDef.getTryBodyDef();
            Variable result = new BodyProcessor(tryBodyDef).run(scraper, context);
            debug(tryBodyDef, scraper, result);

            return result;
        } catch(BaseException e) {
            if ( scraper.getLogger().isInfoEnabled() ) {
                scraper.getLogger().info("Exception caught with try processor: " + e.getMessage());
            }
            BaseElementDef catchValueDef = tryDef.getCatchValueDef();
            Variable result = new BodyProcessor(catchValueDef).run(scraper, context);
            debug(catchValueDef, scraper, result);

            return result;
        }
    }

}