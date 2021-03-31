package software.amazon.iotwireless.partneraccount;

import software.amazon.awssdk.services.iotwireless.model.AssociateAwsAccountWithPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.GetPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdatePartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.ListPartnerAccountsRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateAwsAccountFromPartnerAccountRequest;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Translator {

    //Translate from SDK SidewalkResponse to ResourceModel SidewalkResponse
    static software.amazon.iotwireless.partneraccount.SidewalkAccountInfoWithFingerprint
    translateToSidewalkResponse(final software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfoWithFingerprint model) {
        software.amazon.iotwireless.partneraccount.SidewalkAccountInfoWithFingerprint response =
                new software.amazon.iotwireless.partneraccount.SidewalkAccountInfoWithFingerprint();
        response.setAmazonId(model.amazonId());
        response.setFingerprint(model.fingerprint());
        response.setArn(model.arn());
        return response;
    }

    //Translate from ResourceModel Sidewalk to SDK Sidewalk
    static software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfo
    translateToSidewalkAccountInfo(final ResourceModel model) {
        if (model.getSidewalk() == null) {
            return null;
        }
        software.amazon.iotwireless.partneraccount.SidewalkAccountInfo info = model.getSidewalk();
        return software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfo.builder()
                .amazonId(model.getPartnerAccountId())
                .appServerPrivateKey(info.getAppServerPrivateKey())
                .build();
    }

    //Translate from ResourceModel SidewalkUpdateAccount to SDK SidewalkUpdateAccount
    static software.amazon.awssdk.services.iotwireless.model.SidewalkUpdateAccount
    translateToSidewalkUpdateAccount(final ResourceModel model) {
        if (model.getSidewalkUpdate() == null) {
            return null;
        }
        software.amazon.iotwireless.partneraccount.SidewalkUpdateAccount update = model.getSidewalkUpdate();
        return software.amazon.awssdk.services.iotwireless.model.SidewalkUpdateAccount.builder()
                .appServerPrivateKey(update.getAppServerPrivateKey())
                .build();
    }

    //Translate from SDK SidewalkAccountInfo to ResourceModel SidewalkAccountInfo
    static software.amazon.iotwireless.partneraccount.SidewalkAccountInfo
    translateToSidewalk(final software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfo model) {
        software.amazon.iotwireless.partneraccount.SidewalkAccountInfo info =
                new software.amazon.iotwireless.partneraccount.SidewalkAccountInfo();
        info.setAppServerPrivateKey(model.appServerPrivateKey());
        return info;
    }

    //Translate from ResourceModel Tag to SDK Tag
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.partneraccount.Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.partneraccount.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag =
                    software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build();
            newTagCollection.add(newTag);
        }

        return newTagCollection;
    }

    static AssociateAwsAccountWithPartnerAccountRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.partneraccount.Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return AssociateAwsAccountWithPartnerAccountRequest.builder()
                .sidewalk(translateToSidewalkAccountInfo(model))
                .clientRequestToken(clientRequestToken)
                .tags(translateTag(tags))
                .build();
    }

    static GetPartnerAccountRequest translateToReadRequest(final ResourceModel model) {
        return GetPartnerAccountRequest.builder()
                .partnerAccountId(model.getPartnerAccountId())
                .partnerType(BaseHandlerStd.Type.Sidewalk.toString())
                .build();
    }

    static ListPartnerAccountsRequest translateToListRequest(final String nextToken) {
        return ListPartnerAccountsRequest.builder()
                .nextToken(nextToken)
                .build();
    }

    static UpdatePartnerAccountRequest translateToFirstUpdateRequest(final ResourceModel model) {
        return UpdatePartnerAccountRequest.builder()
                .sidewalk(translateToSidewalkUpdateAccount(model))
                .partnerAccountId(model.getPartnerAccountId())
                .partnerType(model.getPartnerType())
                .build();
    }

    static DisassociateAwsAccountFromPartnerAccountRequest translateToDeleteRequest(final ResourceModel model) {
        return DisassociateAwsAccountFromPartnerAccountRequest.builder()
                .partnerAccountId(model.getPartnerAccountId())
                .partnerType(BaseHandlerStd.Type.Sidewalk.toString())
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .partnerAccountId(model.getPartnerAccountId())
                .arn(model.getArn())
                .sidewalk(model.getSidewalk())
                .accountLinked(model.getAccountLinked())
                .tags(model.getTags())
                .build();
    }
}
