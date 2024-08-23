import http from 'k6/http';
import { randomIntBetween, randomItem  } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import { check, sleep } from 'k6';
import { vu } from 'k6/execution';

export const options = {
  scenarios: {
    concert: {
      executor: 'constant-vus',
      vus: 100,
      duration: '60s',
    },
  },
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

  
  let concertsRes = http.get('http://localhost:8080/concerts?page=0',{
    headers: {
      'TOKEN': token
    }
  });
  let concert = JSON.parse(concertsRes.body)[0];

  sleep(1);

  let concertScheduleRes = http.get('http://localhost:8080/concert/schedules?concertId=' + concert.id,{
    headers: {
      'TOKEN': token
    }
  });
  let concertSchedule = JSON.parse(concertScheduleRes.body)[randomIntBetween(0, 2)];

  sleep(1);

  let concertSeatRes = http.get('http://localhost:8080/concert/seats?concertScheduleId=' + concertSchedule.id,{
    headers: {
      'TOKEN': token
    }
  });

  let concertSeats = JSON.parse(concertSeatRes.body).filter(seat => !seat.reserved);
  let reservation = null;
  for (let i = 0; i < 1000; i++) {
    sleep(1);

    let selectedSeat = randomItem(concertSeats);
    let reservationRes = http.post('http://localhost:8080/reservations?userId=' + vu.idInTest, 
      JSON.stringify({
        seatIds: [selectedSeat.id]
      }),{
      headers: {
        'Content-Type': 'application/json',
        'TOKEN': token
      }
    });

    let reservationBody = JSON.parse(reservationRes.body);
    if(reservationBody.code && reservationBody.code == '400') continue;
    else {
      reservation = reservationBody;
      break;
    };
  }

  if(reservation == null) return; // 예약 실패 (잔여 좌석 없음 등)

  sleep(1);
  
  let assetChargeRes = http.patch('http://localhost:8080/asset/charge?userId=' + vu.idInTest, 
    JSON.stringify({
      amount: 100000000
    }),{
    headers: {
      'Content-Type': 'application/json'
    }
  });
  sleep(1);

  let paymentRes = http.post('http://localhost:8080/payment?userId=' + vu.idInTest, 
    JSON.stringify({
      reservationId: reservation.reservationId
    }),{
    headers: {
      'Content-Type': 'application/json',
      'TOKEN': token
    }
  });

  check(paymentRes, {
    'Payment Success!': (res) => {
      return JSON.parse(res.body).paidAmount != undefined
    }
  })
}