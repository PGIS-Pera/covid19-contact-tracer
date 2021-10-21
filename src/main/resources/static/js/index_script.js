document.getElementById("currentYear").innerHTML = new Date().getFullYear();

$.getJSON('https://www.hpb.health.gov.lk/api/get-current-statistical', function (data) {

    var now_date = (data.data.update_date_time);

    document.getElementById("date").innerHTML = now_date;
    document.getElementById("totRecov").innerHTML = (data.data.local_recovered);
    document.getElementById("totDeth").innerHTML = (data.data.local_deaths);
    document.getElementById("totActiv").innerHTML = (data.data.local_active_cases);
    document.getElementById("totCases").innerHTML = (data.data.local_active_cases) + (data.data.local_recovered) + (data.data.local_deaths);

    document.getElementById("dailyNew").innerHTML = (data.data.local_new_cases);
    document.getElementById("DailyRecov").innerHTML = 1500;
    document.getElementById("dailyDeth").innerHTML = (data.data.local_new_deaths);

    document.getElementById("glbRecovered").innerHTML = (data.data.global_recovered);
    document.getElementById("glbDeth").innerHTML = (data.data.global_deaths);
    document.getElementById("glbActive").innerHTML = (data.data.global_total_cases) - (data.data.global_recovered) - (data.data.global_deaths);
    document.getElementById("glbTotal").innerHTML = (data.data.global_total_cases);

});

function sliceSize(dataNum, dataTotal) {
    return (dataNum / dataTotal) * 360;
}

function addSlice(id, sliceSize, pieElement, offset, sliceID, color) {
    $(pieElement).append("<div class='slice " + sliceID + "'><span></span></div>");
    var offset = offset - 1;
    var sizeRotation = -179 + sliceSize;

    $(id + " ." + sliceID).css({
        "transform": "rotate(" + offset + "deg) translate3d(0,0,0)"
    });

    $(id + " ." + sliceID + " span").css({
        "transform": "rotate(" + sizeRotation + "deg) translate3d(0,0,0)",
        "background-color": color
    });
}

function iterateSlices(id, sliceSize, pieElement, offset, dataCount, sliceCount, color) {
    var
        maxSize = 179,
        sliceID = "s" + dataCount + "-" + sliceCount;

    if (sliceSize <= maxSize) {
        addSlice(id, sliceSize, pieElement, offset, sliceID, color);
    } else {
        addSlice(id, maxSize, pieElement, offset, sliceID, color);
        iterateSlices(id, sliceSize - maxSize, pieElement, offset + maxSize, dataCount, sliceCount + 1, color);
    }
}

function createPie(id) {
    var
        listData = [],
        listTotal = 0,
        offset = 0,
        i = 0,
        pieElement = id + " .pie-chart__pie"
    dataElement = id + " .pie-chart__legend"

    color = [
        "cornflowerblue",
        "olivedrab",
        "orange",
        "tomato",
        "crimson",
        "purple",
        "turquoise",
        "forestgreen",
        "navy"
    ];

    color = shuffle(color);

    $(dataElement + " span").each(function () {
        listData.push(Number($(this).html()));
    });

    for (i = 0; i < listData.length; i++) {
        listTotal += listData[i];
    }

    for (i = 0; i < listData.length; i++) {
        var size = sliceSize(listData[i], listTotal);
        iterateSlices(id, size, pieElement, offset, i, 0, color[i]);
        $(dataElement + " li:nth-child(" + (i + 1) + ")").css("border-color", color[i]);
        offset += size;
    }
}

function shuffle(a) {
    var j, x, i;
    for (i = a.length; i; i--) {
        j = Math.floor(Math.random() * i);
        x = a[i - 1];
        a[i - 1] = a[j];
        a[j] = x;
    }

    return a;
}

function createPieCharts() {
    createPie('.pieID--localstat');
    createPie('.pieID--dailystat');
    createPie('.pieID--globalstat');
}

createPieCharts();
