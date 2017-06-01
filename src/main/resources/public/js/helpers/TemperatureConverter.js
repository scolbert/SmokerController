module.exports.convertKelvinToFarenheit = (kelvinTemp) => {
    return ((9/5)*(kelvinTemp - 273) + 32);
}

module.exports.convertFarenheitToKelvin = (farenheitTemp) => {
    return (farenheitTemp/1.8) + (459.4/1.8);
}
