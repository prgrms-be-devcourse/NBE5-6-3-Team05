const clap = () => {
  console.log();
  console.log('👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏👏');
  console.log();
}

const odd = '홀수';
const even = '짝수';
const hmd = {
  username: '하명도',
  age: 15
}

let count = 0;

const counter = () => {
  count++;
  console.dir(count);
}

export {odd, even, hmd, counter}
export default clap;

