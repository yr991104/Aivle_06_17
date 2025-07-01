// src/pages/PointSystem.jsx
import React, { useState } from 'react';
import AppBar from '../components/AppBar';
import Footer from '../components/Footer';

const PointSystem = () => {
  const [userId, setUserId] = useState('');
  const [pointAmount, setPointAmount] = useState('');
  const [pointBalance, setPointBalance] = useState(1000); // 초기값 예시
  const [pointHistory, setPointHistory] = useState([]);

  const addHistory = (type, amount) => {
    const entry = {
      type,
      amount: parseInt(amount),
      date: new Date().toLocaleString(),
    };
    setPointHistory([entry, ...pointHistory]);
  };

  const handleGivePoint = () => {
    if (!userId || !pointAmount) return alert('정보를 입력하세요');
    setPointBalance(pointBalance + parseInt(pointAmount));
    addHistory('지급', pointAmount);
  };

  const handleDeductPoint = () => {
    if (!userId || !pointAmount) return alert('정보를 입력하세요');
    if (parseInt(pointAmount) > pointBalance) return alert('포인트 부족');
    setPointBalance(pointBalance - parseInt(pointAmount));
    addHistory('차감', pointAmount);
  };

  return (
    <div className="min-h-screen flex flex-col bg-white font-sans">
      <AppBar />
      <main className="flex-1 p-10">
        <h1 className="text-3xl font-bold mb-6">포인트 시스템</h1>

        <div className="space-y-4 max-w-lg">
          <input
            type="text"
            placeholder="User ID 입력"
            className="border px-3 py-2 w-full rounded"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
          />

          <input
            type="number"
            placeholder="포인트 금액 입력"
            className="border px-3 py-2 w-full rounded"
            value={pointAmount}
            onChange={(e) => setPointAmount(e.target.value)}
          />

          <div className="flex gap-4">
            <button onClick={handleGivePoint} className="bg-green-500 text-white px-4 py-2 rounded">
              포인트 지급
            </button>
            <button onClick={handleDeductPoint} className="bg-red-500 text-white px-4 py-2 rounded">
              포인트 차감
            </button>
          </div>

          <div className="mt-6">
            <h2 className="text-xl font-semibold">현재 포인트: {pointBalance}P</h2>
          </div>

          <div className="mt-4">
            <h2 className="text-lg font-semibold mb-2">포인트 이력</h2>
            <ul className="space-y-2">
              {pointHistory.map((item, idx) => (
                <li key={idx} className="border p-2 rounded bg-gray-50">
                  [{item.date}] {item.type} - {item.amount}P
                </li>
              ))}
            </ul>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default PointSystem;
