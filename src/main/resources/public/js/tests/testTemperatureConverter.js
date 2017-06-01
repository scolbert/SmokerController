const converter =  require('../helpers/TemperatureConverter.js');
const assert = require('assert');

describe('convertKelvinToFarenheit', function(){
    it('returns 68 when given 293', function(){
        assert(converter.convertKelvinToFarenheit(293) === 68);
    })
})

describe('convertFarenheitToKelvin', function(){
    it('returns 310 when given 98.6', function(){
        console.log(converter.convertFarenheitToKelvin(98.6));
        assert(converter.convertFarenheitToKelvin(98.6) === 310)
    })
})