$(document).ready(() => {
    changePageAndSize();
    $('[type="date"]').prop('max', function () {
        return new Date().toJSON().split('T')[0];
    });
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/turn/?pageSize=${evt.target.value}&page=1`);
    });
}

let html_first_part = `<table class="table table-hover table-striped" id="myTable" >
                <thead class="thead-inverse" >
                <tr >
                <th >Index</th >
                <th >Person Code</th >
                <th >Person Name</th >
                <th >Identified Date</th >
                <th >View</th >
                </tr >
                </thead >
                <tbody >`;
let html_last_part = ` </tbody ></table >`
let html_middle_part = ``;
let html_no_alert = `<div class="row" >
                       <div class="col-sm-12" >
                         <div class="alert alert-warning alert-dismissible fade show"
                              role="alert" >
                           <button aria-label="Close" class="close" data-dismiss="alert"
                                   type="button" >
                             <span aria-hidden="true" >&times;</span >
                           </button >
                           <h3 class="text-center text-danger" >
                             <strong >There is no turn according to your provided details.
                             </strong >
                           </h3 >
                         </div >
                       </div >
                     </div >`;

$("#selectParameter").change(function () {
    $("#tableShow").text('');
    $("#searchInput").val('');
})

$("#searchInput").keyup(function () {
    //todo need to check this turn search using person
    let url = $("#searchUrl").val();
    let select_parameter_value = $("#selectParameter").val();
    let enter_value = $(this).val();
    let person = {};

    if (select_parameter_value === null) {
        swal({
            title: "Time to take attention !",
            icon: "warning",
            text: `Please Select Search Parameter before enter value. `,
        });
        $(this).val('');
    }

    if (enter_value.length >= 3 && select_parameter_value.length > 0) {
        if (select_parameter_value === "name") {
            person.name = enter_value;
        }
        if (select_parameter_value === "number") {
            person.number = enter_value;
        }
        if (mobileRegex.test(select_parameter_value) && select_parameter_value === "mobile") {
            person.mobile = enter_value;
        }
        console.log(person)
        $.post(url, person,
            function (data, status) {
                console.log(data)
                $("#tableShow").text('');
                if (data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        html_middle_part = html_middle_part + `
                    <tr>
                      <th > ${i + 1}</th >
                      <th > ${data[i].person.code}</th >
                      <th > ${data[i].person.name}</th >
                      <th > ${data[i].identifiedDate}</th >
                      <th >
                          <a class="btn btn-success btn-sm" href="${'/turn/' + data[i].id}" >
                            <i class="fa fa-folder-open" ></i >&nbsp;View
                          </a >
                          <a class="btn btn-primary btn-sm" href="${'/turn/edit/' + data[i].id}" >
                            <i class="fa fa-edit" ></i >&nbsp;Edit
                          </a >                          
                          <a class="btn btn-info btn-sm" href="${'/turn/attempt/' + data[i].id}" >
                              <i class="fas fa-head-side-virus"></i>&nbsp;New Attempt
                          </a >
                      </th >
                    </tr >`;
                    }
                    $("#tableShow").append(html_first_part + html_middle_part + html_last_part);
                }
                if (data.length === 0 && status === 'success') {
                    $("#tableShow").append(html_no_alert);
                }
            },
            'json');
        html_middle_part = '';
    }
})


