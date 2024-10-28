<H1>:wrench: 개발 환경</H1>
<ul>
  <li>Java 17</li>
  <li>Spring Boot 3.3.4</li>
  <li>Gradle</li>
  <li>MySQL 8.0.39</li>
  <li>AWS EC2, AWS RDS</li>
  <li>Docker</li>
</ul>

<H1>:wrench: 프로젝트 트리 구조</H1>
<p><strong>mindshare</strong> (root Project)</p>
<p>├── <strong>common</strong> (공통 기능)</p>
<p>├── <strong>domain</strong> (entity 관리)</p>
<p>├── <strong>main</strong> (application 실행)</p>
<p>├── <strong>post</strong> (게시판)</p>
<p>├── <strong>user</strong> (사용자)</p>

<H1>:rocket: 프로젝트 실행 방법</H1>
<ol>
    <li>Proejct Git Clone</li>
    <li>application.yml에서 Database url, username, password 설정</li>
    <li>mindshare/MySQL_Script Download 후, MySQL Workbench에서 스크립트를 읽어서 Table/data 생성</li>
    <li>mindshare/main/src/main/java/com/hsj/aft/main/MainApplication 실행</li>
</ol>
<p>※ AWS, Docker를 통해 배포를 해서, 배포 환경에서 테스트를 진행하셔도 됩니다.</p>

<H1>:book: API 엔드포인트 목록</H1>
<p><strong>mindshare</strong> (root Project)</p>
<p>├── <strong>common</strong> (공통 기능)</p>
<p>├── <strong>domain</strong> (entity 관리)</p>
<p>├── <strong>main</strong> (application 실행)</p>
<p>├── <strong>post</strong> (게시판)</p>
<p>├── <strong>user</strong> (사용자)</p>
