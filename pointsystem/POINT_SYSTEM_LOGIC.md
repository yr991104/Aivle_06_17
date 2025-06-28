Point System 로직 구현
개요
포인트 시스템은 다음 비즈니스 규칙에 따라 전자책 플랫폼의 사용자 포인트를 관리합니다.

포인트 분배 규칙
1. 가입 보너스 포인트
KT Members: 5,000 포인트

Normal Members: 1,000 포인트

2. 전자책 읽기 접근 및 포인트 차감
Membership Required: 회원 (Normal 또는 KT)만 책을 읽을 수 있습니다.

Subscription Status:

Subscribed: 0 포인트 (무료 읽기)

Non-subscribed: 전자책 1권당 10 포인트 차감

구현 세부 사항
이벤트 기반 아키텍처
시스템은 포인트 연산을 처리하기 위해 Kafka 이벤트를 사용합니다:

SignedUp Event: 사용자가 가입할 때 트리거됩니다.

회원 유형 (KT vs Normal)을 확인합니다.

적절한 보너스 포인트를 지급합니다.

ViewHistory Event: 사용자가 전자책을 읽을 때 트리거됩니다.

사용자가 회원인지 확인합니다 (읽기 필수).

구독 상태 (Subscribed vs Non-subscribed)를 확인합니다.

Non-subscribed 회원에게만 포인트를 차감합니다.

핵심 구성 요소
도메인 클래스
UserPoint: 사용자 포인트의 메인 애그리게이트 루트

PointHistory: 모든 포인트 거래를 추적합니다.

GivePointCommand: 포인트를 지급하기 위한 커맨드

ReducePointCommand: 포인트를 차감하기 위한 커맨드

ViewHistory: 읽은 내역에 대한 내용용

비즈니스 로직 (PolicyHandler)
wheneverSignedUp_CheckMembership(): 가입 보너스 포인트를 처리합니다.

wheneverViewHistory_ReducePointsForReading(): 읽기 접근 및 포인트 차감을 처리합니다.

포인트 연산
UserPoint.givePoint(): 사용자 계정에 포인트를 추가합니다.

UserPoint.reducePoint(): 사용자 계정에서 포인트를 차감합니다.

비즈니스 규칙 요약
회원 유형
KT: 5,000 가입 보너스가 있는 프리미엄 회원

NORMAL: 1,000 가입 보너스가 있는 일반 회원

구독 상태
subscribed: 전자책 무료 읽기 (0 포인트)

non-subscribed: 전자책 유료 읽기 (10 포인트)

접근 제어
회원 (KT 또는 Normal)만 책을 읽을 수 있습니다.

비회원은 읽기 차단됩니다.

이벤트 흐름
사용자 가입 → SignedUp event → 포인트 시스템이 보너스 포인트를 지급합니다.

사용자 전자책 읽기 → ViewHistory event → 포인트 시스템이 멤버십 및 구독을 확인하고, 해당되는 경우 포인트를 차감합니다.

데이터베이스 스키마
UserPoint_table: 사용자 포인트 잔액을 저장합니다.

PointHistory: 설명과 함께 모든 포인트 거래를 추적합니다.























































# Point System Logic Implementation

## Overview
The point system manages user points in the e-book platform with the following business rules:

## Point Distribution Rules

### 1. Sign-up Bonus Points
- **KT Members**: 5,000 points
- **Normal Members**: 1,000 points

### 2. E-book Reading Access & Point Deduction
- **Membership Required**: Only members (normal or KT) can read books
- **Subscription Status**: 
  - **Subscribed**: 0 points (free reading)
  - **Non-subscribed**: 10 points per e-book read

## Implementation Details

### Event-Driven Architecture
The system uses Kafka events to handle point operations:

1. **SignedUp Event**: Triggers when a user signs up
   - Checks membership type (KT vs normal)
   - Gives appropriate bonus points

2. **ViewHistory Event**: Triggers when a user reads an e-book
   - Checks if user is a member (required for reading)
   - Checks subscription status (subscribed vs non-subscribed)
   - Deducts points only for non-subscribed members

### Key Components

#### Domain Classes
- `UserPoint`: Main aggregate root for user points
- `PointHistory`: Tracks all point transactions
- `GivePointCommand`: Command for giving points
- `ReducePointCommand`: Command for reducing points
- `ViewHistory`: Event for e-book reading

#### Business Logic (PolicyHandler)
- `wheneverSignedUp_CheckMembership()`: Handles sign-up bonus points
- `wheneverViewHistory_ReducePointsForReading()`: Handles reading access and point deduction

### Point Operations
- `UserPoint.givePoint()`: Adds points to user account
- `UserPoint.reducePoint()`: Deducts points from user account

## Business Rules Summary

### Membership Types
- **KT**: Premium membership with 5,000 sign-up bonus
- **NORMAL**: Standard membership with 1,000 sign-up bonus

### Subscription Status
- **subscribed**: Free e-book reading (0 points)
- **non-subscribed**: Paid e-book reading (10 points)

### Access Control
- Only members (KT or normal) can read books
- Non-members are blocked from reading

## Event Flow
1. User signs up → `SignedUp` event → Point system gives bonus points
2. User reads e-book → `ViewHistory` event → Point system checks membership and subscription, then deducts points if applicable

## Database Schema
- `UserPoint_table`: Stores user point balances
- `PointHistory`: Tracks all point transactions with descriptions 