/**
 *
 *   Copyright (C) 2014 Paul Cech
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package bigshots.people_helping_people.graph.models;


import bigshots.people_helping_people.graph.utils.Utils;

/**
 * Created by paul on 13.08.14.
 */
public class StandardValue {

    private static final float DEF_STANDARD_VALUE_INDICATOR_STROKE = 2f;
    private static final int DEF_STANDARD_VALUE_COLOR = 0xFF00FF00;
    private int mColor;
    private float mValue;
    private int mY;
    /**
     * @param _stroke The stroke height in dp
     */

    private float mStroke;

    /**
     * @param _color
     * @param _value
     * @param _stroke The stroke height in dp
     */
    public StandardValue(int _color, float _value, float _stroke) {
        mColor = _color;
        mValue = _value;
        mStroke = Utils.dpToPx(_stroke);
    }

    public StandardValue(float _value) {
        mColor = DEF_STANDARD_VALUE_COLOR;
        mValue = _value;
        mStroke = Utils.dpToPx(DEF_STANDARD_VALUE_INDICATOR_STROKE);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int _color) {
        mColor = _color;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float _value) {
        mValue = _value;
    }

    public int getY() {
        return mY;
    }

    public void setY(int _y) {
        mY = _y;
    }

    public float getStroke() {
        return mStroke;
    }

    /**
     * @param _stroke The stroke height in dp
     */
    public void setStroke(float _stroke) {
        mStroke = Utils.dpToPx(_stroke);
    }
}
