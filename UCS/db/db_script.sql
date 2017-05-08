use ucs

drop table dbo.ucs_data
create table dbo.ucs_data (
	device_id varchar(16) not null,
	db_type int not null,
	e_date datetime not null,
	q_value float,
	M1_value float,
	M2_value float,
	t1_value float,
	t2_value float,
	P1_value float,
	P2_value float,
	Tp_value float
)

drop table dbo.ucs_err_data
create table dbo.ucs_err_data (
	device_id varchar(16) not null,
	db_type int not null,
	e_date datetime not null,
	e_code int
)

alter procedure dbo.insertData
 @filename varchar(1024)
as

   declare @bulk_insert varchar(8000)	
   exec('bulk insert ucs.dbo.ucs_data
   from '''+@filename+'''
   WITH 
      (
         FIELDTERMINATOR = ''\t'',
         ROWTERMINATOR = ''|''
      )')
   
go
select * from ucs_data
select * from ucs_err_data order by e_date

insertData 'C:\UCS\Project\UCS\report.txt'

alter procedure dbo.insertErrData
 @filename varchar(1024)
as
  
   declare @bulk_insert varchar(8000)	
   exec('bulk insert ucs.dbo.ucs_err_data
   from '''+@filename+'''
   WITH 
      (
         FIELDTERMINATOR = ''\t'',
         ROWTERMINATOR = ''|''
      )')
   
go
create proc dbo.truncateTables 
as
	truncate table ucs_data
	truncate table ucs_err_data


go
if '2005-11-17 08'=any (select substring(convert(varchar(32),uerr.e_date,20),1,13) from ucs_err_data uerr where uerr.e_code in (122))
print 'OK'

alter proc dbo.generateReport
	@device_id varchar(16),
	@db_type int,
	@start_date datetime,
	@end_date datetime
as
	set nocount on

	declare @ondbtype int
	
	select @ondbtype = case @db_type 
			     when 0 then 13 
			     when 1 then 10  
			     when 2 then 7  
			     when 3 then 4  
			     else 1
			     end
	if @db_type=4 begin
		select 	e_date, e_code
		from ucs_err_data where db_type = 4 and device_id = @device_id
			and (e_code between 51 and 127)
			and e_date between @start_date and @end_date order by e_date
	end else begin

	select 	e_date,q_value,M1_value,M2_value, M1_value-M2_value as M1M2, M2_value-M1_value as M2M1,t1_value,t2_value, t1_value-t2_value as t1t2,P1_value, P2_value, Tp_value,
	case when 
		substring(convert(varchar(32),ucs_data.e_date,20),1,@ondbtype)=any(select substring(convert(varchar(32),uerr.e_date,20),1,@ondbtype) from ucs_err_data uerr where uerr.e_code in (61,64,65,66,67,68,69,71,73,74,76,79,81,88,90,91,93,94,106,114,119,120,121) and uerr.e_date between @start_date and @end_date)
	then 'E' end as Emsg,
	case when 
		substring(convert(varchar(32),ucs_data.e_date,20),1,@ondbtype)=any(select substring(convert(varchar(32),uerr.e_date,20),1,@ondbtype) from ucs_err_data uerr where uerr.e_code in (122) and uerr.e_date between @start_date and @end_date)
	then 'U' end as Umsg,
	case when 
		substring(convert(varchar(32),ucs_data.e_date,20),1,@ondbtype)=any(select substring(convert(varchar(32),uerr.e_date,20),1,@ondbtype) from ucs_err_data uerr where uerr.e_code in (96) and uerr.e_date between @start_date and @end_date)
	then 'D' end as Dmsg,
	case when 
		substring(convert(varchar(32),ucs_data.e_date,20),1,@ondbtype)=any(select substring(convert(varchar(32),uerr.e_date,20),1,@ondbtype) from ucs_err_data uerr where uerr.e_code in (82,84,85,87) and uerr.e_date between @start_date and @end_date)
	then 'G' end as Gmsg

	from ucs_data where db_type = @db_type and device_id = @device_id
			and e_date between @start_date and @end_date order by e_date

	select sum(q_value) as Qsum,sum(M1_value) as M1sum,sum(M2_value) as M2sum, 
		sum(case when M1_value-M2_value<0 then 0 else M1_value-M2_value end) as M1M2sum,
		sum(case when M2_value-M1_value<0 then 0 else M2_value-M1_value end) as M2M1sum, 
		sum(t1_value) as t1sum, sum(t2_value) as t2sum, 
		sum(case when t1_value-t2_value<0 then 0 else t1_value-t2_value end) as t1t2sum ,
		sum(P1_value) as P1sum,sum(P2_value) as P2sum, sum(Tp_value) as TpSum
	from ucs_data where db_type = @db_type and device_id = @device_id
			and e_date between @start_date and @end_date


	select max(e_date) as e_date, sum(Q_value),sum(M1_value),sum(M2_value),sum(Tp_value) from ucs_data
		where db_type = @db_type and device_id = @device_id
			and e_date<=@end_date

	select max(e_date) as e_date , sum(Q_value) as Qsum,sum(M1_value) as M1sum,sum(M2_value) as M2sum,sum(Tp_value) as Tpsum from ucs_data
		where db_type = @db_type and device_id = @device_id
			and e_date<@start_date
	end
go

exec generateReport N'00000001', 1, '2005-01-01', '2006-03-30'
exec generateReport N'00000001', 4, '2005-01-01', '2006-03-30'