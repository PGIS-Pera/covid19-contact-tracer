let STATUS = [];
let PATIENT_COUNT = [];
let COLORS = [];

function showTableData() {
    let myTab = document.getElementById('summaryReport');
    for (let i = 1; i < myTab.rows.length; i++) {
        // GET THE CELLS COLLECTION OF THE CURRENT ROW.
        let objCells = myTab.rows.item(i).cells;
        STATUS.push(objCells.item(1).innerHTML);
        PATIENT_COUNT.push(parseInt(objCells.item(2).innerHTML));
        COLORS.push('#' + Math.floor(Math.random() * 16777215).toString(16));
    }
}

let canvas = document.getElementById("myChart");
let ctx = canvas.getContext('2d');

let data = {
    labels: STATUS,
    datasets: [
        {
            fill: true,
            backgroundColor: COLORS,
            data: PATIENT_COUNT,
// Notice the borderColor
            borderColor: ['black', 'black'],
            borderWidth: [2, 2]
        }
    ]
};


let options = {
    responsive: true,
    title: {
        display: true,
        text: 'Graphical representation summary data',
        position: 'top'
    },
    rotation: -0.7 * Math.PI
};

$(document).ready(function () {
    showTableData();
    // Chart declaration:
    let myBarChart = new Chart(ctx, {
        type: 'pie',
        data: data,
        options: options
    });
});