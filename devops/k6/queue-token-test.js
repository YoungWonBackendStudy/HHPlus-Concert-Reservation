import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  scenarios: {
    queue: {
      executor: 'per-vu-iterations',
      vus: 300,
      iterations: 2,
      maxDuration: '180s'
    }
  }
};

export default function() {
  let token = '';
  while(true) {
    let tokenRes = http.get('http://localhost:8080/queue/token', {
      headers: {
        'TOKEN': token
      }
    });

    let resBody = JSON.parse(tokenRes.body);
    if(resBody.code && resBody.code == '400' && resBody.message == '토큰이 활성화 되었습니다.') break;
    token = resBody.token;
    sleep(1);
  }
}