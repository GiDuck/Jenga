<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<div class="wrapper">

    <div class="profile-content section">
        <div class="container">
            <div class="row">
                <div class="profile-picture">
                    <form method="post" action="" enctype="multipart/form-data">
                        <div class="fileinput fileinput-new" data-provides="fileinput">
                            <div class="fileinput-new img-no-padding">
                                <img src="${pageContext.request.contextPath}/resources/assets/img/faces/clem-onojeghuo-2.jpg"
                                     alt="...">
                            </div>
                            <div class="fileinput-preview fileinput-exists img-no-padding"></div>
                            <div>
                <span class="btn btn-outline-default btn-file btn-round">
                  <span class="fileinput-new">Change Photo</span>
                  <span class="fileinput-exists">Change</span>
                  <input type="file" name="...">
                </span>
                                <br/>
                                <a href="#paper-kit" class="btn btn-link btn-danger fileinput-exists"
                                   data-dismiss="fileinput"><i class="fa fa-times"></i> Remove</a>
                            </div>
                        </div>
                </div>
            </div>

            <br> <br>

            <div class="row">
                <div class="col-md-6 ml-auto mr-auto">
                    <form class="settings-form">
                        <div class="row">
                            <div class="col-12">
                                <div class="form-group">
                                    <label>NickName</label>
                                    <input type="text" class="form-control border-input" placeholder="닉네임 ">
                                </div>
                            </div>
                            <div class="col-md-6 col-sm-6">
                                <div class="form-group">
                                    <label>Last Email</label>
                                    <input type="text" class="form-control border-input" placeholder="Last Name">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Job Title</label>
                            <input type="text" class="form-control border-input" placeholder="Job Title">
                        </div>
                        <div class="form-group">
                            <label>Description</label>
                            <textarea class="form-control textarea-limited"
                                      placeholder="This is a textarea limited to 150 characters." rows="3"
                                      maxlength="150"></textarea>
                            <h5>
                                <small>
                                    <span id="textarea-limited-message" class="pull-right">150 characters left</span>
                                </small>
                            </h5>
                        </div>
                        <label>Notifications</label>
                        <ul class="notifications">
                            <li class="notification-item">
                                Updates regarding platform changes
                                <input type="checkbox" data-toggle="switch" checked="" data-on-color="info"
                                       data-off-color="info">
                                <span class="toggle"></span>
                            </li>
                            <li class="notification-item">
                                Updates regarding product changes
                                <input type="checkbox" data-toggle="switch" checked="" data-on-color="info"
                                       data-off-color="info">
                                <span class="toggle"></span>
                            </li>
                            <li class="notification-item">
                                Weekly newsletter
                                <input type="checkbox" data-toggle="switch" checked="" data-on-color="info"
                                       data-off-color="info">
                                <span class="toggle"></span>
                            </li>
                        </ul>
                        <div class="text-center">
                            <button type="submit" class="btn btn-wd btn-info btn-round">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    $(document).ready(_ = > {}
    )
    ;

    setNavType("blue");

</script>