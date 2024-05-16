package ma.enset.service;

import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.bankServiceGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcService extends bankServiceGrpc.bankServiceImplBase {

    @Override
    public void convert(Bank.convertCurrencyRequest request, StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
         String form = request.getFrom();
         String to = request.getTo();
         double amount = request.getAmount();

         Bank.convertCurrencyResponse response = Bank.convertCurrencyResponse.newBuilder()
                 .setAmount(amount)
                 .setFrom(form)
                 .setTo(to)
                 .setResult(amount * 11.4)
                 .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
    }

    @Override
    public void getCurrencyStream(Bank.convertCurrencyRequest request, StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
        String form = request.getFrom();
        String to = request.getTo();
        double amount = request.getAmount();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;
                           @Override
                           public void run() {
                                 Bank.convertCurrencyResponse response = Bank.convertCurrencyResponse.newBuilder()
                                        .setAmount(amount)
                                        .setFrom(form)
                                        .setTo(to)
                                        .setResult(amount * Math.random() * 100)
                                        .build();
                                 responseObserver.onNext(response);
                                    count++;
                                    if(count == 10) {
                                        responseObserver.onCompleted();
                                        timer.cancel();
                                    }
                           }
                       }, 1000, 1000);

    }



    @Override
    public StreamObserver<Bank.convertCurrencyRequest> performSteam(StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.convertCurrencyRequest>() {
            double sum = 0 ;
            @Override
            public void onNext(Bank.convertCurrencyRequest convertCurrencyRequest) {
               sum += convertCurrencyRequest.getAmount() * 11.4;
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                Bank.convertCurrencyResponse response = Bank.convertCurrencyResponse.newBuilder()
                        .setResult(sum * 11.4)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }
        };
    }

    @Override
    public StreamObserver<Bank.convertCurrencyRequest> fullStream(StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.convertCurrencyRequest>() {
            @Override
            public void onNext(Bank.convertCurrencyRequest convertCurrencyRequest) {
                Bank.convertCurrencyResponse response = Bank.convertCurrencyResponse.newBuilder()
                        .setAmount(convertCurrencyRequest.getAmount())
                        .setFrom(convertCurrencyRequest.getFrom())
                        .setTo(convertCurrencyRequest.getTo())
                        .setResult(convertCurrencyRequest.getAmount() * Math.random() * 45)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }

        };

    }
}
