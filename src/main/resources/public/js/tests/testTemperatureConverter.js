const converter =  require('../helpers/TemperatureConverter.js');
const assert = require('assert');

describe('convertKelvinToFarenheit', function(){
    it('returns 68 when given 293', function(){
        assert(converter.convertKelvinToFarenheit(293) === 68);
    })
})