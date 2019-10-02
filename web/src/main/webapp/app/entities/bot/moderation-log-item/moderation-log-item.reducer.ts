import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IModerationLogItem, defaultValue } from 'app/shared/model/bot/moderation-log-item.model';

export const ACTION_TYPES = {
  FETCH_MODERATIONLOGITEM_LIST: 'moderationLogItem/FETCH_MODERATIONLOGITEM_LIST',
  FETCH_MODERATIONLOGITEM: 'moderationLogItem/FETCH_MODERATIONLOGITEM',
  CREATE_MODERATIONLOGITEM: 'moderationLogItem/CREATE_MODERATIONLOGITEM',
  UPDATE_MODERATIONLOGITEM: 'moderationLogItem/UPDATE_MODERATIONLOGITEM',
  DELETE_MODERATIONLOGITEM: 'moderationLogItem/DELETE_MODERATIONLOGITEM',
  RESET: 'moderationLogItem/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IModerationLogItem>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ModerationLogItemState = Readonly<typeof initialState>;

// Reducer

export default (state: ModerationLogItemState = initialState, action): ModerationLogItemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MODERATIONLOGITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MODERATIONLOGITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MODERATIONLOGITEM):
    case REQUEST(ACTION_TYPES.UPDATE_MODERATIONLOGITEM):
    case REQUEST(ACTION_TYPES.DELETE_MODERATIONLOGITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MODERATIONLOGITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MODERATIONLOGITEM):
    case FAILURE(ACTION_TYPES.CREATE_MODERATIONLOGITEM):
    case FAILURE(ACTION_TYPES.UPDATE_MODERATIONLOGITEM):
    case FAILURE(ACTION_TYPES.DELETE_MODERATIONLOGITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MODERATIONLOGITEM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MODERATIONLOGITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MODERATIONLOGITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_MODERATIONLOGITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MODERATIONLOGITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/moderation-log-items';

// Actions

export const getEntities: ICrudGetAllAction<IModerationLogItem> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MODERATIONLOGITEM_LIST,
  payload: axios.get<IModerationLogItem>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IModerationLogItem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MODERATIONLOGITEM,
    payload: axios.get<IModerationLogItem>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IModerationLogItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MODERATIONLOGITEM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IModerationLogItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MODERATIONLOGITEM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IModerationLogItem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MODERATIONLOGITEM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
