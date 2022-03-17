create table account (eid bigint not null, roleid bigint not null, password varchar(255), status varchar(255), username varchar(255), primary key (eid, roleid)) engine=InnoDB;
create table apply (jrecruitid bigint not null, cid bigint not null, primary key (jrecruitid, cid)) engine=InnoDB;
create table attendance (eid bigint not null, day date, device_id varchar(255), time_in time, time_out time, primary key (eid)) engine=InnoDB;
create table bonus_list (id bigint not null auto_increment, amount decimal(16,2), name varchar(255), eid bigint, primary key (id)) engine=InnoDB;
create table candidate (id bigint not null auto_increment, email varchar(255), name varchar(255), cv_file_id bigint, primary key (id)) engine=InnoDB;
create table candidate_cv (id bigint not null auto_increment, cv_file_content longblob, cv_file_name varchar(255), cv_file_size bigint, cv_file_type varchar(255), primary key (id)) engine=InnoDB;
create table department (id bigint not null auto_increment, description TEXT, location varchar(255), name varchar(255), people_number integer, type varchar(255), head_of_unit_id bigint, primary key (id)) engine=InnoDB;
create table employee (id bigint not null auto_increment, avatar TEXT, date_of_birth date, email varchar(255), employed_date date, first_name varchar(255), gross_salary decimal(16,2), last_name varchar(255), permanent_address varchar(255), phone varchar(255), pit varchar(255), sex varchar(255), temporary_address varchar(255), position_id bigint, primary key (id)) engine=InnoDB;
create table has (posid bigint not null, jrecruitid bigint, primary key (posid)) engine=InnoDB;
create table insurance (eid bigint not null auto_increment, id bigint not null, typeid bigint not null, city_id bigint, from_date date, issue_date date, kcb_id bigint, number varchar(255), to_date date, primary key (eid, id, typeid)) engine=InnoDB;
create table insurance_type (id bigint not null auto_increment, name varchar(255), rate_of_payment decimal(16,2), primary key (id)) engine=InnoDB;
create table job_recruitment (id bigint not null auto_increment, from_date date, job_description TEXT, note TEXT, status integer, title varchar(255), to_date date, primary key (id)) engine=InnoDB;
create table leave_type (id bigint not null auto_increment, is_paid bit, name varchar(255), primary key (id)) engine=InnoDB;
create table leaves (eid bigint not null, typeid bigint not null, description longtext, from_date date, to_date date, primary key (eid, typeid)) engine=InnoDB;
create table manage (did bigint not null, eid bigint, primary key (did)) engine=InnoDB;
create table managed_by (eid bigint not null, pid bigint, primary key (eid)) engine=InnoDB;
create table payment (eid bigint not null, derived_salary decimal(16,2), mandatory_insurance decimal(16,2), other_income decimal(16,2), payment_date date, personal_tax decimal(16,2), payroll_id bigint, primary key (eid)) engine=InnoDB;
create table payroll (id bigint not null auto_increment, created_time date, name varchar(255), primary key (id)) engine=InnoDB;
create table performs (eid bigint not null, taskid bigint not null, pid bigint not null, primary key (eid, taskid, pid)) engine=InnoDB;
create table position (id bigint not null auto_increment, description varchar(255), max_salary decimal(16,2), min_salary decimal(16,2), name varchar(255), salary_group integer, primary key (id)) engine=InnoDB;
create table project (id bigint not null auto_increment, end_date date, name varchar(255), start_date date, primary key (id)) engine=InnoDB;
create table role (id bigint not null auto_increment, description varchar(255), name varchar(255), primary key (id)) engine=InnoDB;
create table task (id bigint not null auto_increment, pid bigint not null, content TEXT, status varchar(255), title varchar(255), primary key (id, pid)) engine=InnoDB;
create table works_in (eid bigint not null, did bigint, primary key (eid)) engine=InnoDB;
create table works_on (hours bigint, eid bigint not null, pid bigint, primary key (eid)) engine=InnoDB;
alter table account add constraint UK_jmfbj6jbt7rqa7fwtnym5dtla unique (eid);
alter table account add constraint UK_s31qsf8macdltst507k15no3s unique (roleid);
alter table account add constraint FK2neav5gsa773rwxay6s0ib9q2 foreign key (eid) references employee (id);
alter table account add constraint FK4uk2u5ju46nwjwvlwt1jol9ue foreign key (roleid) references role (id);
alter table apply add constraint FKcxn0sjhv6uccn9s48vag9r3p5 foreign key (cid) references candidate (id);
alter table apply add constraint FKsph002n8dnxk1x4vrl41p7jho foreign key (jrecruitid) references job_recruitment (id);
alter table attendance add constraint FK1ja4jj07t8co2pc74j0ckl59o foreign key (eid) references employee (id);
alter table bonus_list add constraint FK8oexta4kt1gvxjws78vpwakqg foreign key (eid) references employee (id);
alter table candidate add constraint FKl5xsqrvwc3sj5hoe8222hbr72 foreign key (cv_file_id) references candidate_cv (id);
alter table department add constraint FKhwihkrs21tavvykrih45c9oit foreign key (head_of_unit_id) references department (id);
alter table employee add constraint FKbc8rdko9o9n1ri9bpdyxv3x7i foreign key (position_id) references position (id);
alter table has add constraint FKfctdmqapnhx5204em7ea0nmbf foreign key (posid) references position (id);
alter table has add constraint FKghjm0yqnsb5lu2f63t7f3lv67 foreign key (jrecruitid) references job_recruitment (id);
alter table insurance add constraint FK3s3edx1le2u73ceh5hcscjao5 foreign key (eid) references employee (id);
alter table insurance add constraint FKi83dhq4dn9sxbs6v624q0jl07 foreign key (typeid) references insurance_type (id);
alter table leaves add constraint FKct2jmtn0m6gdq018w4t66y3w9 foreign key (eid) references employee (id);
alter table leaves add constraint FKpyycia4cuurr3fn7cn6os11lv foreign key (typeid) references leave_type (id);
alter table manage add constraint FKjym3vfmonec4p0qyovpkr303a foreign key (did) references department (id);
alter table manage add constraint FKlhjwqsk9r98rx5kfgnimr620t foreign key (eid) references employee (id);
alter table managed_by add constraint FK5jbgfkpi0w11whfw39ngesvsv foreign key (eid) references employee (id);
alter table managed_by add constraint FKorh3tnkytl3j2gvctrhlhd7eu foreign key (pid) references project (id);
alter table payment add constraint FKklurhil02yqllonk5y5qrl7xc foreign key (eid) references employee (id);
alter table payment add constraint FK84wnkhdh937ks551e1t55gic6 foreign key (payroll_id) references payroll (id);
alter table performs add constraint FK12hsevbq0rlluntg4tbiubm5g foreign key (taskid, pid) references task (id, pid);
alter table performs add constraint FKca4msxgcdy705j4pu5dvfgbwt foreign key (eid) references employee (id);
alter table task add constraint FK6lcyxw45rx12wikvofewqfeb0 foreign key (pid) references project (id);
alter table works_in add constraint FK5du22ku3v4gm4ojenmctj4kl6 foreign key (did) references department (id);
alter table works_in add constraint FKbcjs5q943wf0k4ubptpyy5an2 foreign key (eid) references employee (id);
alter table works_on add constraint FKoascll1lfe51f8720y2eno809 foreign key (eid) references employee (id);
alter table works_on add constraint FKmn2hncgm84lm1dpgosbh6x6bm foreign key (pid) references project (id);
