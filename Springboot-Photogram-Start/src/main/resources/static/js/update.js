// (1) 회원정보 수정
function update(userid,event) {
	event.preventDefault(); //폼태그 액션 막기
//console.log(event);
let data = $("#profileUpdate").serialize(); //form 태그의 id값인 profileUpdate로 form 태그 안의 정보를 가져옴
console.log(data);

$.ajax({ //ajax는 파일을 응답한다.
	type:"put",
	url:`/api/user/${userid}`,
	data:data,
	contentType: "application/x-www-form-urlencoded;charset=utf-8",
	dataType: "json" //json 타입으로 넘겨준다.
}).done(res=>{ //json으로 파싱되서 들어온다, HttpStatus 상태코드 200번대
	console.log("update 성공");
	console.log("성공",res);
	location.href=`/user/${userid}`
}).fail(err=>{//HttpStatus 상태코드 200번대가 아닐 때
	if(err.responseJSON.data == null){
		alert(err.responseJSON.message);
	}else{
	alert(JSON.stringify(err.responseJSON.data)); //errorMap을 뿌린다 
	//.replace("{","").replace("}","").replace("name","이름").replace(/\"/gi,"").replace(":","은 "));	
}

})
}