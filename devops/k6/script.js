import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  scenarios: {
    queue: {
      executor: 'per-vu-iterations',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 50 }, // 10초 동안 50명의 가상 사용자 도달
      ],
      gracefulRampDown: '0s',
      exec: 'queueToken', // scenario1 함수 호출
    },
    concert: {
      executor: 'per-vu-iterations',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 50 }, // 10초 동안 50명의 가상 사용자 도달
      ],
      gracefulRampDown: '0s',
      exec: 'concertReservation',
    },
  },
};

export function scenario1() {
  http.get('https://api1.example.com');
  sleep(1); // 1초 대기
}

export function concertReservation() {
  let token = http.get('https://api2.example.com');
  sleep(1);
}